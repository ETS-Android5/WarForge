package forge.screens.match.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import com.google.common.collect.Lists;

import forge.gui.framework.ICDoc;
import forge.interfaces.IGameController;
import forge.screens.match.CMatchUI;
import forge.screens.match.views.IDevListener;
import forge.screens.match.views.VDev;

/**
 * Controls the combat panel in the match UI.
 *
 * <br><br><i>(C at beginning of class name denotes a control class.)</i>
 *
 */
public final class CDev implements ICDoc {

    private final CMatchUI matchUI;
    private final VDev view;
    public CDev(final CMatchUI matchUI) {
        this.matchUI = matchUI;
        this.view = new VDev(this);
        addListener(view);

        view.getLblUnlimited().addMouseListener(madUnlimited);
        view.getLblViewAll().addMouseListener(madViewAll);
        view.getLblGenerateMana().addMouseListener(madMana);
        view.getLblSetupGame().addMouseListener(madSetup);
        view.getLblDumpGame().addMouseListener(madDump);
        view.getLblTutor().addMouseListener(madTutor);
        view.getLblCardToHand().addMouseListener(madCardToHand);
        view.getLblExileFromHand().addMouseListener(madExileFromHand);
        view.getLblCardToBattlefield().addMouseListener(madCardToBattlefield);
        view.getLblCardToLibrary().addMouseListener(madCardToLibrary);
        view.getLblCardToGraveyard().addMouseListener(madCardToGraveyard);
        view.getLblCardToExile().addMouseListener(madCardToExile);
        view.getLblCastSpell().addMouseListener(madCastASpell);
        view.getLblRepeatAddCard().addMouseListener(madRepeatAddCard);
        view.getLblAddCounterPermanent().addMouseListener(madAddCounter);
        view.getLblSubCounterPermanent().addMouseListener(madSubCounter);
        view.getLblTapPermanent().addMouseListener(madTap);
        view.getLblUntapPermanent().addMouseListener(madUntap);
        view.getLblSetLife().addMouseListener(madLife);
        view.getLblWinGame().addMouseListener(madWinGame);
        view.getLblExileFromPlay().addMouseListener(madExileFromPlay);
        view.getLblRemoveFromGame().addMouseListener(madRemoveFromGame);
        view.getLblRiggedRoll().addMouseListener(madRiggedRoll);
        view.getLblWalkTo().addMouseListener(madWalkToPlane);
    }
    public IGameController getController() {
        return matchUI.getGameController();
    }
    public VDev getView() {
        return view;
    }

    private final List<IDevListener> listeners = Lists.newArrayListWithCapacity(2);
    public void addListener(final IDevListener listener) {
        listeners.add(listener);
    }

    private final MouseListener madUnlimited = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
        	togglePlayUnlimited();
        }
    };
    public void togglePlayUnlimited() {
        final boolean newValue = !view.getLblUnlimited().getToggled();
        getController().cheat().setCanPlayUnlimited(newValue);
        update();
    }

    private final MouseListener madViewAll = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            toggleViewAllCards();
        }
    };
    public void toggleViewAllCards() {
        final boolean newValue = !view.getLblViewAll().getToggled();
        getController().cheat().setViewAllCards(newValue);
        update();
    }

    private final MouseListener madMana = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            generateMana(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void generateMana(boolean empty) {
        getController().cheat().generateMana(empty);
    }

    private final MouseListener madSetup = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            setupGameState(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void setupGameState(boolean lastState) {
        getController().cheat().setupGameState(lastState);
    }

    private final MouseListener madDump = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            dumpGameState(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void dumpGameState(boolean quick) {
        getController().cheat().dumpGameState(quick);
    }

    private final MouseListener madTutor = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            tutorForCard(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void tutorForCard(boolean sideboard) {
        getController().cheat().tutorForCard(sideboard);
    }

    private final MouseListener madCardToHand = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            addCardToHand(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void addCardToHand(boolean mostCommon) {
        getController().cheat().addCardToHand(mostCommon);
    }

    private final MouseListener madCardToLibrary = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            addCardToLibrary(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void addCardToLibrary(boolean mostCommon) {
        getController().cheat().addCardToLibrary(mostCommon);
    }

    private final MouseListener madCardToGraveyard = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            addCardToGraveyard(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void addCardToGraveyard(boolean mostCommon) {
        getController().cheat().addCardToGraveyard(mostCommon);
    }

    private final MouseListener madCardToExile = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            addCardToExile(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void addCardToExile(boolean mostCommon) {
        getController().cheat().addCardToExile(mostCommon);
    }

    private final MouseListener madCastASpell = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            castASpell(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void castASpell(boolean mostCommon) {
        getController().cheat().castASpell(mostCommon);
    }

    private final MouseListener madRepeatAddCard = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            repeatAddCard();
        }
    };
    public void repeatAddCard() {
        getController().cheat().repeatLastAddition();
    }

    private final MouseListener madExileFromHand = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            exileCardsFromHand();
        }
    };
    public void exileCardsFromHand() {
        getController().cheat().exileCardsFromHand();
    }

    private final MouseListener madAddCounter = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            addCounterToPermanent(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void addCounterToPermanent(boolean player) {
        getController().cheat().addCountersToPermanent(player);
    }

    private final MouseListener madSubCounter = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            removeCountersFromPermanent(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void removeCountersFromPermanent(boolean player) {
        getController().cheat().removeCountersFromPermanent(player);
    }

    private final MouseListener madTap = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            tapPermanent(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void tapPermanent(boolean all) {
        getController().cheat().tapPermanents(all);
    }

    private final MouseListener madUntap = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            untapPermanent(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void untapPermanent(boolean all) {
        getController().cheat().untapPermanents(all);
    }

    private final MouseListener madLife = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            setPlayerLife(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void setPlayerLife(boolean maxLife) {
        getController().cheat().setPlayerLife(maxLife);
    }

    private final MouseListener madWinGame = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            winGame(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void winGame(boolean lose) {
        getController().cheat().winGame(lose);
    }

    private final MouseListener madCardToBattlefield = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            addCardToBattlefield(e.getButton() == MouseEvent.BUTTON3);
        }
    };
    public void addCardToBattlefield(boolean mostCommon) {
        getController().cheat().addCardToBattlefield(mostCommon);
    }

    private final MouseListener madExileFromPlay = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            exileCardsFromPlay();
        }
    };
    public void exileCardsFromPlay() {
        getController().cheat().exileCardsFromBattlefield();
    }

    private final MouseListener madRemoveFromGame = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            removeCardsFromGame();
        }
    };
    public void removeCardsFromGame() {
        getController().cheat().removeCardsFromGame();
    }

    private final MouseListener madRiggedRoll = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            riggedPlanerRoll();
        }
    };
    public void riggedPlanerRoll() {
        getController().cheat().riggedPlanarRoll();
    }

    private final MouseListener madWalkToPlane = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            planeswalkTo();
        }
    };
    public void planeswalkTo() {
        getController().cheat().planeswalkTo();
    }

    //========== End mouse listener inits

    @Override
    public void register() {
    }

    /* (non-Javadoc)
     * @see forge.gui.framework.ICDoc#initialize()
     */
    @Override
    public void initialize() {
    }

    /* (non-Javadoc)
     * @see forge.gui.framework.ICDoc#update()
     */
    @Override
    public void update() {
        final IGameController controller = getController();
        if (controller != null) {
            final boolean canPlayUnlimited = controller.canPlayUnlimited();
            final boolean mayLookAtAllCards = controller.mayLookAtAllCards();
            for (final IDevListener listener : listeners) {
                listener.update(canPlayUnlimited, mayLookAtAllCards);
            }
        }
    }
}
