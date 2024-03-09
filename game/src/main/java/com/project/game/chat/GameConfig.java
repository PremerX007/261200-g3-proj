package com.project.game.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class GameConfig {
    public static GameConfig userConfig = GameConfig.builder()
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

    private int m;
    private int n;
    private int init_plan_min;
    private int init_plan_sec;
    private int init_budget;
    private int init_center_dep;
    private int plan_rev_min;
    private int plan_rev_sec;
    private int rev_cost;
    private int max_dep;
    private int interest_pct;
}
