package Controller;

import Parser.EvalError;
import Parser.PlanParser;
import Parser.Statement.Statement;
import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;
import UPBEAT.Position;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class ControllerTest {
    @Test
    void DoneCommand() throws LexicalError, SyntaxError, IOException, EvalError {
        Map<String, Long> execute = new HashMap<>();
        Map<String, Long> expect = new HashMap<>();

        /** Create Simulate 4x4 Territory and 1 Player
         * fix player in position [2,2]
         */
        Territory.instance(4,4,10000,5);
        Player p = new Player("Bob", new Position(2,2),10000,100);

        StringBuilder s = new StringBuilder();
        s.append("t = t + 1\n");
        s.append("if (0) then done else t = t + 1\n");
        s.append("t = t ^ 10");

        expect.put("t", 1024L);

        PlanParser plan = new PlanParser(new PlanTokenizer(new StringReader(s.toString())));
        Statement stm = plan.parse();
        stm.eval(p,execute);
        assertFalse(p.isDone());
        assertEquals(expect,execute);

        s = new StringBuilder();
        s.append("t = t + 1000\n");
        s.append("if (1) then done else t = t + 1\n");
        s.append("wrong = 911");

        expect.put("t", 2024L);

        plan = new PlanParser(new PlanTokenizer(new StringReader(s.toString())));
        stm = plan.parse();
        stm.eval(p,execute);
        assertTrue(p.isDone());
        assertEquals(expect,execute);
    }

    @Nested
    class InfomationExpr {
        static Player p1;
        static Player p2;
        static Player p3;
        @BeforeAll
        static void init() throws LexicalError, SyntaxError, IOException {
            Territory.instance = null;
            Territory.instance(9,9,10000,5);
            p1 = new Player("p1", new Position(2,3),10000,100);
            p2 = new Player("p2", new Position(6,3), 10000,100);
            Territory.instance.addPlayerRegion(p2,new Position(1,5));
            Territory.instance.addPlayerRegion(p2,new Position(4,6));
            p3 = new Player("p3", new Position(7,7), 10000,100);
            Territory.instance.addPlayerRegion(p3,new Position(6,2));
            Territory.instance.addPlayerRegion(p3,new Position(7,3));
        }

        @Nested
        class opponent {
            @Test
            void notSeeBlankRegions(){
                // If Player 3 looking from position [7,7] should not see anyone
                long result = Territory.instance.opponentCheck(p3, new Position(7,7));
                assertEquals(0,result);
            }

            @Test
            void multipleOpponent(){
                /* If Player 1 looking from position [2,3]
                 * will see [1,5] at down       distance=4  should return 42
                 *          [4,6] at downright  distance=3  should return 33
                 *      and [6,3] at upright    distance=2  should return 22
                 *
                 * but method should return the location of the one with the lowest direction number.
                 * test expected : 22
                 */

                long result = Territory.instance.opponentCheck(p1, new Position(2,3));
                assertEquals(22,result);
            }

            @Test
            void sameMinimalDistance(){
                /* If Player 2 looking from position [6,3]
                 * will see [6,2] at upright    distance=1  should return 16
                 *          [7,3] at down       distance=1  should return 14
                 *      and [2,3] at up         distance=4  (not interested, afar)
                 *
                 * this case have 2 regions is same minimal distance
                 * but method should return the location of the one with the lowest direction number.
                 * test expected : 14
                 */
                long result = Territory.instance.opponentCheck(p2, new Position(6,3));
                assertEquals(14,result);
            }
        }

        @Nested
        class nearby {
            @Test
            void correctValue(){
                /* If Player 2 looking from position [6,3]
                 * will see [6,2] at upright    distance=1
                 *          [7,3] at down       distance=1
                 *      and [2,3] at up         distance=4
                 *
                 * If we interest position [2,3]
                 * when call nearby function
                 * should return 100*x+y
                 * x is the distance from the city crew to that region
                 * y is the number of digits in the current deposit
                 *
                 * this test we fix [2,3] have deposit 100 (number of digits = 3)
                 * thus should return 100*4+3
                 * test expected : 403
                 */

                long result = Territory.instance.nearbyCheck(p2, new Position(6,3), 1);
                assertEquals(403,result);
            }

            @Test
            void noOpponentNearby(){
                /* If Player 2 looking from position [6,3] at upright direction
                 * will not see anyone.
                 *
                 * If no opponent owns a region in the given direction, the nearby function should return 0.
                 * test expected : 0
                 */
                long result = Territory.instance.nearbyCheck(p2, new Position(6,3), 2);
                assertEquals(0,result);
            }

        }
    }
}
