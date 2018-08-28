package io.github.agentsoz.ees.agents.bushfire;

import io.github.agentsoz.abmjill.genact.EnvironmentAction;
import io.github.agentsoz.bdimatsim.MATSimModel;
import io.github.agentsoz.jill.lang.Agent;
import io.github.agentsoz.jill.lang.Goal;
import io.github.agentsoz.jill.lang.Plan;
import io.github.agentsoz.jill.lang.PlanStep;
import io.github.agentsoz.util.evac.ActionList;

import java.util.Map;

/*
 * #%L
 * Jill Cognitive Agents Platform
 * %%
 * Copyright (C) 2014 - 2016 by its authors. See AUTHORS file.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

public class PlanInitialResponseWithoutDependents extends Plan {

	BushfireAgent agent = null;
	boolean goingHomeFirst = false;

	public PlanInitialResponseWithoutDependents(Agent agent, Goal goal, String name) {
		super(agent, goal, name);
		this.agent = (BushfireAgent)agent;
		body = steps;
	}

	public boolean context() {
		boolean applicable = (agent.isInitialResponseThresholdBreached() && agent.getDependentInfo() == null)
				? true : false;
		agent.memorise(BushfireAgent.MemoryEventType.DECIDED.name(), BushfireAgent.MemoryEventValue.IS_PLAN_APPLICABLE.name()
				+ ":" + getGoal() + "|" + this.getClass().getSimpleName() + "=" + applicable);
		return applicable;
	}

	PlanStep[] steps = {
			() -> {
				if (agent.isFinalResponseThresholdBreached()) {
					agent.memorise(BushfireAgent.MemoryEventType.DECIDED.name(), BushfireAgent.MemoryEventValue.DONE_FOR_NOW.name());
				} else {
					goingHomeFirst = true;
					post(new GoalGoHome("GoalGoHome"));
					// Now wait till the next step for this goal to finish
				}
			},
			() -> {
				if (goingHomeFirst) {
					// arrived home
				}
			},
	};

	@Override
	public void setPlanVariables(Map<String, Object> vars) {
	}
}