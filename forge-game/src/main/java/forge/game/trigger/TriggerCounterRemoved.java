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

import forge.game.ability.AbilityKey;
import forge.game.card.Card;
import forge.game.card.CounterType;
import forge.game.spellability.SpellAbility;

/**
 * <p>
 * Trigger_CounterRemoved class.
 * </p>
 * 
 * @author Forge
 * @version $Id: TriggerCounterAdded.java 12297 2011-11-28 19:56:47Z jendave $
 */
public class TriggerCounterRemoved extends Trigger {

    /**
     * <p>
     * Constructor for Trigger_CounterRemoved.
     * </p>
     * 
     * @param params
     *            a {@link java.util.HashMap} object.
     * @param host
     *            a {@link forge.game.card.Card} object.
     * @param intrinsic
     *            the intrinsic
     */
    public TriggerCounterRemoved(final java.util.Map<String, String> params, final Card host, final boolean intrinsic) {
        super(params, host, intrinsic);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean performTest(final java.util.Map<String, Object> runParams2) {
        final Card addedTo = (Card) runParams2.get("Card");
        final CounterType addedType = (CounterType) runParams2.get("CounterType");
        final Integer addedNewCounterAmount = (Integer) runParams2.get("NewCounterAmount");

        if (hasParam("ValidCard")) {
            if (!addedTo.isValid(getParam("ValidCard").split(","), this.getHostCard().getController(),
                    this.getHostCard(), null)) {
                return false;
            }
        }

        if (hasParam("CounterType")) {
            final String type = getParam("CounterType");
            if (!type.equals(addedType.toString())) {
                return false;
            }
        }

        if (hasParam("NewCounterAmount")) {
            final String amtString = getParam("NewCounterAmount");
            int amt = Integer.parseInt(amtString);
            if(amt != addedNewCounterAmount.intValue()) {
                return false;
            }
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final void setTriggeringObjects(final SpellAbility sa) {
        sa.setTriggeringObjectsFrom(this, AbilityKey.Card);
    }

    @Override
    public String getImportantStackObjects(SpellAbility sa) {
        StringBuilder sb = new StringBuilder();
        sb.append("Removed from: ").append(sa.getTriggeringObject(AbilityKey.Card));
        return sb.toString();
    }
}
