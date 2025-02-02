/*
 * Forge: Play Magic: the Gathering.
 * Copyright (C) 2011  Forge Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package forge.game.trigger;

import java.util.Map;

import forge.game.ability.AbilityKey;
import forge.game.card.Card;
import forge.game.card.CardLists;
import forge.game.spellability.SpellAbility;

/**
 * <p>
 * Trigger_AttackerBlocked class.
 * </p>
 * 
 * @author Forge
 * @version $Id$
 */
public class TriggerAttackerBlocked extends Trigger {

    /**
     * <p>
     * Constructor for Trigger_AttackerBlocked.
     * </p>
     * 
     * @param params
     *            a {@link java.util.HashMap} object.
     * @param host
     *            a {@link forge.game.card.Card} object.
     * @param intrinsic
     *            the intrinsic
     */
    public TriggerAttackerBlocked(final java.util.Map<String, String> params, final Card host, final boolean intrinsic) {
        super(params, host, intrinsic);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean performTest(final Map<String, Object> runParams2) {
        if (hasParam("ValidCard")) {
            if (!matchesValid(runParams2.get("Attacker"), getParam("ValidCard").split(","),
                    getHostCard())) {
                return false;
            }
        }

        if (hasParam("MinBlockers")) {
            if ((int)runParams2.get("NumBlockers") < Integer.valueOf(getParam("MinBlockers"))) {
                return false;
            }
        }

        if (hasParam("ValidBlocker")) {
            @SuppressWarnings("unchecked")
            int count = CardLists.getValidCardCount(
                    (Iterable<Card>) runParams2.get("Blockers"),
                    getParam("ValidBlocker"),
                    getHostCard().getController(), getHostCard()
            );

            if ( count == 0 ) {
                return false;
            }
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final void setTriggeringObjects(final SpellAbility sa) {
        sa.setTriggeringObjectsFrom(
            this,
            AbilityKey.Attacker,
            AbilityKey.Blockers,
            AbilityKey.Defender,
            AbilityKey.DefendingPlayer,
            AbilityKey.NumBlockers
        );
    }

    @Override
    public String getImportantStackObjects(SpellAbility sa) {
        StringBuilder sb = new StringBuilder();
        sb.append("Attacker: ").append(sa.getTriggeringObject(AbilityKey.Attacker)).append(", ");
        sb.append("Number Blockers: ").append(sa.getTriggeringObject(AbilityKey.NumBlockers));
        return sb.toString();
    }
}
