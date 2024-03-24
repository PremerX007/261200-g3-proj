package com.project.game.chat;

import com.project.game.repo.src.Controller.Player;
import com.project.game.repo.src.Controller.Territory;
import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.PlanParser;
import com.project.game.repo.src.Parser.Statement.Statement;
import com.project.game.repo.src.Tokenizer.LexicalError;
import com.project.game.repo.src.Tokenizer.PlanTokenizer;
import com.project.game.repo.src.Tokenizer.SyntaxError;
import com.project.game.repo.src.Tokenizer.Tokenizer;
import com.project.game.repo.src.UPBEAT.Game;
import com.project.game.rest.CommandType;
import com.project.game.rest.GameState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class GameRoomController {
    @Autowired
    private final GameService gameService;

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(final Message message, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        if(PlayerList.user.isEmpty()) message.setAdmin(true);
        PlayerList.addUser(message);
        GroupMessage g = new GroupMessage();
        for(Message u: PlayerList.user){
            g.addMsg(u);
        }
        gameService.sendtoGroup(g);
        log.info("Player \"" + message.getSender() + "\" connect to server");
        return message;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(Message message){
        return message;
    }

    @MessageMapping("/chat.status")
    public void status(Message message) throws LexicalError, SyntaxError, EvalError, IOException, InterruptedException {
        Message tmp = PlayerList.findUser(message.getSender());

        GroupMessage g = new GroupMessage();
        if(message.getType() == MessageType.START){
            g.setStart(true);
            tmp.setType(MessageType.READY);
            List<Message> tmpList = new ArrayList<>();
            for(Message u: PlayerList.user){
                if(u.getType().equals(MessageType.READY)) {
                    g.addMsg(u);
                    tmpList.add(u);
                }
            }
            PlayerList.assignUser(tmpList);
        }else{
            tmp.setType(message.getType());
            int readyAmount = 0;
            for(Message u: PlayerList.user){
                if(u.getType().equals(MessageType.READY)) readyAmount++;
                g.addMsg(u);
            }
            g.setReadyPerson(readyAmount);
        }
        gameService.sendtoGroup(g);
        if(message.getType() == MessageType.START) GameInitial();
    }

    @GetMapping("/player")
    public GroupMessage returnPlayer(){
        GroupMessage g = new GroupMessage();
        for(Message u: PlayerList.user){
            g.addMsg(u);
        }
        return g;
    }

    @GetMapping("/game/config")
    public GameConfig returnUserConfig(){
        return GameConfig.userConfig;
    }

    @PutMapping("/game/config/set")
    public GameConfig putPlayerConfig(@RequestBody GameConfig config){
        return GameConfig.userConfig = config;
    }

    @GetMapping("/game/config/reset")
    public GameConfig returnInitConfig(){
        return GameConfig.builder()
                .m(9)
                .n(9)
                .init_plan_min(5)
                .init_plan_sec(0)
                .init_budget(10000)
                .init_center_dep(100)
                .plan_rev_min(30)
                .plan_rev_sec(0)
                .rev_cost(100)
                .max_dep(1000000)
                .interest_pct(5)
                .build();
    }

    @GetMapping("game/territory")
    public ArrayTerritory getTerritory(){
        return ArrayTerritory.builder().arr(Territory.instance.getRegions()).build();
    }

    @PostMapping("game/plan/check")
    public PlanCheckResponse checkConstructionPlan(@RequestBody String body) throws IOException {
        try {
            Tokenizer tkz = new PlanTokenizer(new StringReader(body));
            PlanParser plan = new PlanParser(tkz);
            plan.parse();
        } catch (LexicalError | NoSuchElementException | SyntaxError e) {
            return PlanCheckResponse.builder().result(e.getMessage()+" ❌").build();
        }
        return PlanCheckResponse.builder().result("Your construction plan can be used ✅").build();
    }

    @PostMapping("game/plan/set")
    public PlanCheckResponse RestSetNewConstructionPlan(@RequestBody RestConstPlan body) throws IOException, EvalError, InterruptedException {
        Statement stm = null;
        try {
            Tokenizer tkz = new PlanTokenizer(new StringReader(body.getPlan()));
            PlanParser plan = new PlanParser(tkz);
            stm = plan.parse();
        } catch (LexicalError | NoSuchElementException | SyntaxError e) {
            if(!(Integer.parseInt(body.getTime_min()) == 0 && Integer.parseInt(body.getTime_sec()) == 0)){
                return PlanCheckResponse.builder().result(e.getMessage() + " ❌").build();
            }
        }
        Player player = Game.instance.findPlayer(body.getSender());
        if(stm != null) player.setStatement(stm);
        new Thread(() -> {
            try {
                continueGameState();
            } catch (EvalError | InterruptedException e) {
                log.info(e.getMessage());
            }
        }).start();
        if(stm == null) return PlanCheckResponse.builder().result("Timeout, and your construction plan is invalid game will use you old plan ❌").build();
        return PlanCheckResponse.builder().result("Construction plan saved ✅").build();
    }

    @PostMapping("game/plan/reuse")
    public RestReusePlan reusePlan(@RequestBody RestReusePlan body){
        Player player = Game.instance.findPlayer(body.getSender());
        player.setUseOldStatement(body.isReuse());
        if(body.isReuse())
            log.info("Player \""+ body.getSender() +"\" reuse plan");
        else
            log.info("Player \""+ body.getSender() +"\" need to change plan");
        body.setStatus(body.isReuse() ? "Reuse old construction plan" : "You don't want to reuse old construction plan");
        return body;
    }

    @PostMapping("game/plan/init")
    public PlanCheckResponse RestInitConstructionPlan(@RequestBody RestConstPlan body) throws IOException, EvalError, InterruptedException {
        Statement stm = null;
        try {
            Tokenizer tkz = new PlanTokenizer(new StringReader(body.getPlan()));
            PlanParser plan = new PlanParser(tkz);
            stm = plan.parse();
        } catch (LexicalError | NoSuchElementException | SyntaxError e) {
            if(!(Integer.parseInt(body.getTime_min()) == 0 && Integer.parseInt(body.getTime_sec()) == 0)){
                return PlanCheckResponse.builder().result(e.getMessage() + " ❌").build();
            }
        }
        Player player = Game.instance.findPlayer(body.getSender());
        player.setStatement(stm);
        player.setConstInit(true);
        player.setUseOldStatement(true);
        gameService.sendGameData();
        new Thread(() -> {
            try {
                gameState();
            } catch (EvalError | InterruptedException e) {
                log.info(e.getMessage());
            }
        }).start();
        if(stm == null) return PlanCheckResponse.builder().result("Timeout, and your construction plan is invalid please wait for next turn to edit ❌").build();
        return PlanCheckResponse.builder().result("Construction plan saved ✅").build();
    }

    public void GameInitial() throws LexicalError, SyntaxError, EvalError, IOException, InterruptedException {
        // game init
        Game.instance(gameService);
        gameService.sendGameData();

        // init construction plan signal
        Thread.sleep(5000);
        var tmp = GameState.builder().command(CommandType.INIT).build();
        gameService.sendGameState(tmp);

    }
    public void gameState() throws EvalError, InterruptedException {
        if(Game.instance.playerInitAll()){
            while (Game.instance.nowTurn().isUseOldStatement()) {
                Player p = Game.instance.nowTurn();
                var tmp = GameState.builder()
                        .command(CommandType.GAME)
                        .nowturn(p.getName())
                        .reusePlan(p.isUseOldStatement())
                        .build();
                gameService.sendGameState(tmp);
                if(p.getStatement() != null) p.run();
                Game.instance.nextTurn();
                gameService.sendGameData();
                Thread.sleep(2000);
            }
            var tmp = GameState.builder()
                    .command(CommandType.GAME)
                    .nowturn(Game.instance.nowTurn().getName())
                    .reusePlan(Game.instance.nowTurn().isUseOldStatement())
                    .build();
            gameService.sendGameState(tmp);
        }
    }


    public void continueGameState() throws EvalError, InterruptedException {
        Player p = Game.instance.nowTurn();
        if(p.getStatement() != null) p.run();
        Game.instance.nextTurn();
        gameState();
    }
}
