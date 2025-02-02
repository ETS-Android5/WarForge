package forge.game.trigger;

import java.util.List;
import java.util.Map;

import forge.game.ability.AbilityKey;
import forge.game.card.*;
import forge.game.spellability.SpellAbility;
import forge.game.zone.ZoneType;

public class TriggerChangesZoneAll extends Trigger {

    public TriggerChangesZoneAll(Map<String, String> params, Card host, boolean intrinsic) {
        super(params, host, intrinsic);
    }

    @Override
    public boolean performTest(Map<String, Object> runParams2) {
        final CardZoneTable table = (CardZoneTable) runParams2.get("Cards");

        return !filterCards(table).isEmpty();
    }

    @Override
    public void setTriggeringObjects(SpellAbility sa) {
        final CardZoneTable table = (CardZoneTable) getFromRunParams(AbilityKey.Cards);

        CardCollection allCards = this.filterCards(table);

        sa.setTriggeringObject(AbilityKey.Cards, allCards);
        sa.setTriggeringObject(AbilityKey.Amount, allCards.size());
    }

    @Override
    public String getImportantStackObjects(SpellAbility sa) {
        StringBuilder sb = new StringBuilder();
        sb.append("Amount: ").append(sa.getTriggeringObject(AbilityKey.Amount));
        return sb.toString();
    }

    private CardCollection filterCards(CardZoneTable table) {
        ZoneType destination = null;
        List<ZoneType> origin = null;

        if (hasParam("Destination") && !getParam("Destination").equals("Any")) {
            destination = ZoneType.valueOf(getParam("Destination"));
        }

        if (hasParam("Origin") && !getParam("Origin").equals("Any")) {
            origin = ZoneType.listValueOf(getParam("Origin"));
        }

        final String valid = this.getParamOrDefault("ValidCards", null);

        return table.filterCards(origin, destination, valid, getHostCard(), null);
    }
}
