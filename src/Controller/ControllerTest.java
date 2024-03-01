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
        Player p = new Player("Bob", new Position(2,2),10000);

        StringBuilder s = new StringBuilder();
        s.append("t = t + 1\n");
        s.append("if (0) then done else t = t + 1\n");
        s.append("t = t ^ 10");

        // If done command work correctly t = t + 1 in if-else and t = t ^ 10 should evaluate
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

        // If done command work correctly t = t + 1 in if-else and t = t ^ 10 should not evaluate
        expect.put("t", 2024L);

        plan = new PlanParser(new PlanTokenizer(new StringReader(s.toString())));
        stm = plan.parse();
        stm.eval(p,execute);
        assertTrue(p.isDone());
        assertEquals(expect,execute);
    }

    @Nested
    class InformationExpr {
        static Player p1;
        static Player p2;
        static Player p3;
        @BeforeAll
        static void init() throws LexicalError, SyntaxError, IOException {
            /* Create simulate territory 9x9 and 3 players
             * Player 1 have regions -> [2,3]
             * Player 2 have regions -> [1,5] [4,6] [6,3]
             * Player 3 have regions -> [6,2] [7,3] [7,7]
             *
             */
            Territory.instance = null;
            Territory.instance(9,9,10000,5);

            p1 = new Player("p1", new Position(2,3),10000);
            Territory.instance.setStartPlayer(p1, new Position(2,3), 100);

            p2 = new Player("p2", new Position(6,3), 10000);
            Territory.instance.addPlayerRegion(p2,new Position(1,5));
            Territory.instance.addPlayerRegion(p2,new Position(4,6));

            p3 = new Player("p3", new Position(7,7), 10000);
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
                 *
                 * Expected test result : 22
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
                 *
                 * Expected test result : 14
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
                 *
                 * Expected test result : 403
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
                 * Expected test result : 0
                 */
                long result = Territory.instance.nearbyCheck(p2, new Position(6,3), 2);
                assertEquals(0,result);
            }

        }
    }

    @Nested
    class SpecialVarEval {
        static Player p1;
        static Player p2;
        @BeforeAll
        static void init() throws LexicalError, SyntaxError, IOException {
            /* Create simulate territory 9x5 and 2 players
             * Player 1 have regions -> [2,3] [2,4] [2,5] [3,4]
             * Player 2 have regions -> [2,2] (but lose)
             * region at [2,2] will not be owned by a player (but have deposit 350 in that region).
             *
             * and "move" function should complete when run this test.
             * If move function is not completed yet, it will cannot move the city crew and some testcase will fail.
             */
            Territory.instance = null;
            Territory.instance(9,5,10000,5);

            p1 = new Player("p1", new Position(2,3),10000);
            Territory.instance.setStartPlayer(p1, new Position(2,3), 100);
            Territory.instance.addPlayerRegion(p1,new Position(2,4));
            Territory.instance.addPlayerRegion(p1,new Position(2,5));
            Territory.instance.addPlayerRegion(p1,new Position(3,4));


            p2 = new Player("p2", new Position(2,2),10000);
            Territory.instance.setStartPlayer(p2, new Position(2,2), 350);
            p2.playerLose();
        }
        @Test
        void rows() throws EvalError {
            /* When evaluate rows variable, the number of rows in the territory is returned.
             * rows in this territory is 9
             *
             * Expected test result : 9
             */
            Variable var = new Variable("rows");
            long result = var.eval(p1,new HashMap<>());
            assertEquals(9, result);
        }

        @Test
        void cols() throws EvalError {
            /* When evaluate cols variable, the number of column in the territory is returned.
             * column in this territory is 5
             *
             * Expected test result : 5
             */
            Variable var = new Variable("cols");
            long result = var.eval(p1,new HashMap<>());
            assertEquals(5, result);
        }

        @Test
        void currow() throws EvalError {
            /* When evaluate currow variable, the current row number of the city crew is returned.
             * start position of city crew is [2,3]
             * current row number of the city crew is 2
             *
             * Expected test result : 2
             */
            Variable var = new Variable("currow");
            long result = var.eval(p1,new HashMap<>());
            assertEquals(2, result);

            /* Try to move city crew to [3,4]
             * current row number of the city crew is 3
             *
             * If "move" function is not completed yet, it will cannot move the city crew and this test will fail.
             * Expected test result : 3
             */
            p1.getCrew().move(2);
            p1.getCrew().move(4);

            result = var.eval(p1,new HashMap<>());
            assertEquals(3,result);

            // Reset the city crew for another test
            p1.getCrew().goBackCityCenter();
        }

        @Test
        void curcol() throws EvalError {
            /* When evaluate curcol variable, the current column number of the city crew is returned.
             * start position of city crew is [2,3]
             * current row number of the city crew is 3
             *
             * Expected test result : 3
             */
            Variable var = new Variable("curcol");
            long result = var.eval(p1,new HashMap<>());
            assertEquals(3, result);

            /* Try to move city crew to [2,5]
             * current column number of the city crew is 5
             *
             * If "move" function is not completed yet, it will cannot move the city crew and this test will fail.
             * Expected test result : 5
             */
            p1.getCrew().move(2);
            p1.getCrew().move(3);
            result = var.eval(p1,new HashMap<>());
            assertEquals(5,result);

            // Reset the city crew for another test
            p1.getCrew().goBackCityCenter();
        }

        @Test
        void budget() throws EvalError {
            /* When evaluate budget variable, the player's remaining budget is returned.
             * current player budget is 10000
             * because just started the game player received init budget
             *
             * Expected test result : 10000
             */
            Variable var = new Variable("budget");
            long result = var.eval(p1,new HashMap<>());
            assertEquals(10000, result);

            /* Try to move city crew to [2,5]
             * start at [2,3]@(city center) -> [2,4] -> [3,4] -> [2,5]
             * Player should pay cost for move command 3 time
             * Current player budget should be 9997
             *
             * If "move" function is not completed yet, it will cannot move the city crew and this test will fail.
             *
             * Expected test result : 9997
             */
            p1.getCrew().move(2);
            p1.getCrew().move(4);
            p1.getCrew().move(2);
            result = var.eval(p1,new HashMap<>());
            assertEquals(9997, result);

            // Reset the city crew for another test
            p1.getCrew().goBackCityCenter();
        }

        @Test
        void deposit() throws EvalError {
            /* When evaluate deposit variable, the current deposit on the current region
             * occupied by the city crew is returned.
             *
             * current region deposit (at city center) is 100
             * because just started the game region received init city center budget
             *
             * Expected test result : 100
             */
            Variable var = new Variable("deposit");
            long result = var.eval(p1,new HashMap<>());
            assertEquals(100, result);

            /* When the city crew stand in region belongs to no players
             *
             * make position [2,2] is region that are not owned by any player and that region have deposit 350.
             * if evaluate deposit variable, the current deposit (if any) should be negated before returned.
             *
             * current region deposit is 350
             *
             * Expected test result : -350
             */
            p1.getCrew().move(6);

            var = new Variable("deposit");
            result = var.eval(p1,new HashMap<>());
            assertEquals(-350, result);
        }
    }
}
