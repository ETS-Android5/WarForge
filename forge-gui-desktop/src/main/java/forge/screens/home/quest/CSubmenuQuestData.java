package forge.screens.home.quest;

import forge.UiCommand;
import forge.deck.Deck;
import forge.deck.DeckSection;
import forge.game.GameFormat;
import forge.gui.framework.ICDoc;
import forge.item.PaperCard;
import forge.model.FModel;
import forge.properties.ForgeConstants;
import forge.quest.*;
import forge.quest.StartingPoolPreferences.PoolType;
import forge.quest.data.DeckConstructionRules;
import forge.quest.data.GameFormatQuest;
import forge.quest.data.QuestData;
import forge.quest.data.QuestPreferences.QPref;
import forge.quest.io.QuestDataIO;
import forge.screens.bazaar.CBazaarUI;
import forge.toolbox.FOptionPane;
import forge.util.Localizer;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Controls the quest data submenu in the home UI.
 *
 * <br><br><i>(C at beginning of class name denotes a control class.)</i>
 *
 */
@SuppressWarnings("serial")
public enum CSubmenuQuestData implements ICDoc {
    SINGLETON_INSTANCE;

    private final Map<String, QuestData> arrQuests = new HashMap<>();

    private final VSubmenuQuestData view = VSubmenuQuestData.SINGLETON_INSTANCE;
    private final List<String> customFormatCodes = new ArrayList<>();
    private final List<String> customPrizeFormatCodes = new ArrayList<>();

    private final UiCommand cmdQuestSelect = new UiCommand() {
        @Override public void run() {
            changeQuest();
        }
    };
    private final UiCommand cmdQuestUpdate = new UiCommand() {
        @Override public void run() {
            update();
        }
    };

    private List<Byte> preferredColors = new ArrayList<>();
    private PoolType poolType = PoolType.BALANCED;
    private boolean includeArtifacts = true;
    private int numberOfBoosters = 0;

    @Override
    public void register() {
    }

    /* (non-Javadoc)
     * @see forge.control.home.IControlSubmenu#update()
     */
    @Override
    public void initialize() {
        view.getBtnEmbark().setCommand(
                new UiCommand() { @Override public void run() { newQuest(); } });

        // disable the very powerful sets -- they can be unlocked later for a high price
        final List<String> unselectableSets = new ArrayList<>();
        unselectableSets.add("LEA");
        unselectableSets.add("LEB");
        unselectableSets.add("MBP");
        unselectableSets.add("VAN");
        unselectableSets.add("ARC");
        unselectableSets.add("PC2");

        view.getBtnCustomFormat().setCommand(new UiCommand() {
            @Override
            public void run() {
                final DialogChooseSets dialog = new DialogChooseSets(customFormatCodes, unselectableSets, false);
                dialog.setOkCallback(new Runnable() {
                    @Override
                    public void run() {
                        customFormatCodes.clear();
                        customFormatCodes.addAll(dialog.getSelectedSets());
                    }
                });
            }
        });

        view.getBtnSelectFormat().setCommand(new UiCommand() {
            @Override
            public void run() {
                final DialogChooseFormats dialog = new DialogChooseFormats();
                dialog.setOkCallback(new Runnable() {
                    @Override
                    public void run() {
                        customFormatCodes.clear();
                        Set<String> sets = new HashSet<>();
                        for(GameFormat format:dialog.getSelectedFormats()){
                            sets.addAll(format.getAllowedSetCodes());
                        }
                        customFormatCodes.addAll(sets);
                    }
                });
            }
        });

        view.getBtnPrizeCustomFormat().setCommand(new UiCommand() {
            @Override
            public void run() {
                final DialogChooseSets dialog = new DialogChooseSets(customPrizeFormatCodes, unselectableSets, false);
                dialog.setOkCallback(new Runnable() {
                    @Override
                    public void run() {
                        customPrizeFormatCodes.clear();
                        customPrizeFormatCodes.addAll(dialog.getSelectedSets());
                    }
                });
            }
        });

        view.getBtnPrizeSelectFormat().setCommand(new UiCommand() {
            @Override
            public void run() {
                final DialogChooseFormats dialog = new DialogChooseFormats();
                dialog.setOkCallback(new Runnable() {
                    @Override
                    public void run() {
                        customPrizeFormatCodes.clear();
                        Set<String> sets = new HashSet<>();
                        for(GameFormat format:dialog.getSelectedFormats()){
                            sets.addAll(format.getAllowedSetCodes());
                        }
                        customPrizeFormatCodes.addAll(sets);
                    }
                });
            }
        });

        view.getBtnPreferredColors().setCommand(new UiCommand() {
            @Override
            public void run() {
                final DialogChoosePoolDistribution colorChooser = new DialogChoosePoolDistribution(preferredColors, poolType, includeArtifacts);
                colorChooser.show(new UiCommand() {
                    @Override
                    public void run() {
                        preferredColors = colorChooser.getPreferredColors();
                        poolType = colorChooser.getPoolType();
                        includeArtifacts = colorChooser.includeArtifacts();
                        numberOfBoosters = colorChooser.getNumberOfBoosters();
                    }
                });
            }
        });

    }

    /* (non-Javadoc)
     * @see forge.control.home.IControlSubmenu#update()
     */
    @Override
    public void update() {

        final VSubmenuQuestData view = VSubmenuQuestData.SINGLETON_INSTANCE;
        final File dirQuests = new File(ForgeConstants.QUEST_SAVE_DIR);
        final QuestController qc = FModel.getQuest();
        ArrayList<String> restorableQuests = new ArrayList<>();

        // Iterate over files and load quest data for each.
        final FilenameFilter takeDatFiles = new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".dat");
            }
        };
        final File[] arrFiles = dirQuests.listFiles(takeDatFiles);
        arrQuests.clear();
        for (final File f : arrFiles) {
            try {
                System.out.println(String.format("About to load quest (%s)... ", f.getName()));
                arrQuests.put(f.getName(), QuestDataIO.loadData(f));
            } catch(IOException ex) {
                ex.printStackTrace();
                System.out.println(String.format("Error loading quest data (%s).. skipping for now..", f.getName()));
                restorableQuests.add(f.getName());
            }
        }

        // Populate list with available quest data.
        view.getLstQuests().setQuests(new ArrayList<>(arrQuests.values()));

        // If there are quests available, force select.
        if (!arrQuests.isEmpty()) {
            final String questName = FModel.getQuestPreferences().getPref(QPref.CURRENT_QUEST);

            // Attempt to select previous quest.
            if (arrQuests.get(questName) != null) {
                view.getLstQuests().setSelectedQuestData(arrQuests.get(questName));
            }
            else {
                view.getLstQuests().setSelectedIndex(0);
            }

            // Drop into AllZone.
            qc.load(view.getLstQuests().getSelectedQuest());
        }
        else {
            qc.load(null);
        }

        view.getLstQuests().setSelectCommand(cmdQuestSelect);
        view.getLstQuests().setDeleteCommand(cmdQuestUpdate);
        view.getLstQuests().setEditCommand(cmdQuestUpdate);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.getBtnEmbark().requestFocusInWindow();
            }
        });

    }

    /**
     * The actuator for new quests.
     */
    private void newQuest() {
        final Localizer localizer = Localizer.getInstance();
        final VSubmenuQuestData view = VSubmenuQuestData.SINGLETON_INSTANCE;
        final int difficulty = view.getSelectedDifficulty();

        final QuestMode mode = view.isFantasy() ? QuestMode.Fantasy : QuestMode.Classic;

        Deck dckStartPool = null;
        GameFormat fmtStartPool = null;
        final QuestWorld startWorld = FModel.getWorlds().get(view.getStartingWorldName());

        final GameFormat worldFormat = (startWorld == null ? null : startWorld.getFormat());

        if (worldFormat == null) {
            switch(view.getStartingPoolType()) {
            case Sanctioned:
                fmtStartPool = view.getRotatingFormat();
                break;

            case Casual:
            case CustomFormat:
                if (customFormatCodes.isEmpty()) {
                    if (!FOptionPane.showConfirmDialog(localizer.getMessage("lblNotFormatDefined"))) {
                        return;
                    }
                }
                fmtStartPool = customFormatCodes.isEmpty() ? null : new GameFormatQuest("Custom", customFormatCodes, null); // chosen sets and no banned cards
                break;

            case DraftDeck:
            case SealedDeck:
            case Cube:
                dckStartPool = view.getSelectedDeck();
                if (null == dckStartPool) {
                    FOptionPane.showMessageDialog(localizer.getMessage("lbldckStartPool"), localizer.getMessage("lblCannotStartaQuest"), FOptionPane.ERROR_ICON);
                    return;
                }
                break;

            case Precon:
                dckStartPool = QuestController.getPrecons().get(view.getSelectedPrecon()).getDeck();
                break;

            case Complete:
            default:
                // leave everything as nulls
                break;
            }
        }
        else {
            fmtStartPool = worldFormat;
        }

        GameFormat fmtPrizes;

        final StartingPoolType prizedPoolType = view.getPrizedPoolType();
        if (null == prizedPoolType) {
            fmtPrizes = fmtStartPool;
            if (null == fmtPrizes && dckStartPool != null) { // build it form deck
                final Set<String> sets = new HashSet<>();
                for (final Entry<PaperCard, Integer> c : dckStartPool.getMain()) {
                    sets.add(c.getKey().getEdition());
                }
                if (dckStartPool.has(DeckSection.Sideboard)) {
                    for (final Entry<PaperCard, Integer> c : dckStartPool.get(DeckSection.Sideboard)) {
                        sets.add(c.getKey().getEdition());
                    }
                }
                fmtPrizes = new GameFormat(localizer.getMessage("lblFromDeck"), sets, null);
            }
        }
        else {
            switch(prizedPoolType) {
            case Complete:
                fmtPrizes = null;
                break;
            case Casual:
            case CustomFormat:
                if (customPrizeFormatCodes.isEmpty()) {
                    if (!FOptionPane.showConfirmDialog(localizer.getMessage("lblNotFormatDefined"))) {
                        return;
                    }
                }
                fmtPrizes = customPrizeFormatCodes.isEmpty() ? null : new GameFormat("Custom Prizes", customPrizeFormatCodes, null); // chosen sets and no banned cards
                break;
            case Sanctioned:
                fmtPrizes = view.getPrizedRotatingFormat();
                break;
            default:
                throw new RuntimeException("Should not get this result");
            }
        }

        final StartingPoolPreferences userPrefs = new StartingPoolPreferences(poolType, preferredColors, includeArtifacts, view.startWithCompleteSet(), view.allowDuplicateCards(), numberOfBoosters);

        String questName;
        while (true) {
            questName = FOptionPane.showInputDialog(localizer.getMessage("MsgQuestNewName") + ":",  localizer.getMessage("TitQuestNewName"));
            if (questName == null) { return; }

            questName = QuestUtil.cleanString(questName);

            if (questName.isEmpty()) {
                FOptionPane.showMessageDialog(localizer.getMessage("lblQuestNameEmpty"));
                continue;
            }
            if (getAllQuests().get(questName + ".dat") != null) {
                FOptionPane.showMessageDialog(localizer.getMessage("lblQuestExists"));
                continue;
            }
            break;
        }

        //Apply the appropriate deck construction rules for this quest
        DeckConstructionRules dcr = DeckConstructionRules.Default;

        if(VSubmenuQuestData.SINGLETON_INSTANCE.isCommander()){
            dcr = DeckConstructionRules.Commander;
        }

        final QuestController qc = FModel.getQuest();

        qc.newGame(questName, difficulty, mode, fmtPrizes, view.isUnlockSetsAllowed(), dckStartPool, fmtStartPool, view.getStartingWorldName(), userPrefs, dcr);
        FModel.getQuest().save();

        // Save in preferences.
        FModel.getQuestPreferences().setPref(QPref.CURRENT_QUEST, questName + ".dat");
        FModel.getQuestPreferences().save();

        update();

    }

    /** Changes between quest data files. */
    private void changeQuest() {

        FModel.getQuest().load(VSubmenuQuestData.SINGLETON_INSTANCE.getLstQuests().getSelectedQuest());

        // Save in preferences.
        FModel.getQuestPreferences().setPref(QPref.CURRENT_QUEST, FModel.getQuest().getName() + ".dat");
        FModel.getQuestPreferences().save();

        CSubmenuDuels.SINGLETON_INSTANCE.update();
        CSubmenuChallenges.SINGLETON_INSTANCE.update();
        CSubmenuQuestDecks.SINGLETON_INSTANCE.update();
        CSubmenuQuestDraft.SINGLETON_INSTANCE.update();
	    CBazaarUI.SINGLETON_INSTANCE.update();

    }

    private Map<String, QuestData> getAllQuests() {
        return arrQuests;
    }

}
