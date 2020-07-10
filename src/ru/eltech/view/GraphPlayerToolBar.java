package ru.eltech.view;

import ru.eltech.logic.Algorithm;
import ru.eltech.logic.Graph;
import ru.eltech.logic.GraphPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ru.eltech.logic.GraphPlayer.State.*;

/**
 * Панель управления анимацией, в отдельном классе, так как IntelliJ не умеет нормально работать с нажатиями на кнопки
 */
public final class GraphPlayerToolBar extends JToolBar implements ActionListener, ChangeListener {
    private MainWindow parent;

    private final JPanel upperPanel = new JPanel();
    private final JCheckBox toolBarSetReverseCheckBox = new JCheckBox("Проводить инвертирование графа мгновенно", true);
    private final JButton toolBarAutoButton = new JButton(new ImageIcon(getClass().getResource("/resources/icons/repeat-24px.png")));
    private final JButton toolBarPlayButton = new JButton(new ImageIcon(getClass().getResource("/resources/icons/play_arrow-24px.png")));
    private final JButton toolBarPauseButton = new JButton(new ImageIcon(getClass().getResource("/resources/icons/pause-24px.png")));
    private final JButton toolBarStepBackwardButton = new JButton(new ImageIcon(getClass().getResource("/resources/icons/skip_previous-24px.png")));
    private final JButton toolBarStepForwardButton = new JButton(new ImageIcon(getClass().getResource("/resources/icons/skip_next-24px.png")));
    private final JButton toolBarStopButton = new JButton(new ImageIcon(getClass().getResource("/resources/icons/stop-24px.png")));
    private final Separator separator = new Separator();
    private final JLabel speedLabel = new JLabel("Delay: ");
    private final JSlider toolBarSpeedSlider = new JSlider(1, 1000, 500);
    private final JPanel bottomPanel = new JPanel();
    private final JLabel progressLabel = new JLabel("Progress: ");
    private final JProgressBar toolBarProgressBar = new JProgressBar(1, 100);

    public GraphPlayerToolBar() {
        toolBarSetReverseCheckBox.addActionListener(this);
        toolBarAutoButton.addActionListener(this);
        toolBarPlayButton.addActionListener(this);
        toolBarPlayButton.setMnemonic('Q');
        toolBarPlayButton.setToolTipText("Auto (Alt+Q)");
        toolBarPauseButton.addActionListener(this);
        toolBarPauseButton.setMnemonic('Q');
        toolBarPauseButton.setToolTipText("Pause (Alt+Q)");
        toolBarStepBackwardButton.addActionListener(this);
        toolBarStepBackwardButton.setMnemonic('W');
        toolBarStepBackwardButton.setToolTipText("Step Backward (Alt+W)");
        toolBarStepForwardButton.addActionListener(this);
        toolBarStepForwardButton.setMnemonic('E');
        toolBarStepForwardButton.setToolTipText("Step Forward (Alt+E)");
        toolBarStopButton.addActionListener(this);
        toolBarStopButton.setMnemonic('R');
        toolBarStopButton.setToolTipText("Stop (Alt+R)");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOrientation(SwingConstants.VERTICAL);
        add(upperPanel);
        //upperPanel.add(toolBarAutoButton);
        upperPanel.add(toolBarSetReverseCheckBox);
        upperPanel.add(toolBarPlayButton);
        upperPanel.add(toolBarPauseButton);
        upperPanel.add(toolBarStepBackwardButton);
        upperPanel.add(toolBarStepForwardButton);
        upperPanel.add(toolBarStopButton);
        upperPanel.add(separator);
        upperPanel.add(speedLabel);
        toolBarSpeedSlider.setValue(500);
        upperPanel.add(toolBarSpeedSlider);
        toolBarSpeedSlider.addChangeListener(this);

        add(bottomPanel);
        bottomPanel.add(progressLabel);
        bottomPanel.add(toolBarProgressBar);

        //toolBar1.setMinimumSize(new Dimension(613, 64));

        //toolBar1.setPreferredSize(new Dimension(777, 100));
        //panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        //panel1.setPreferredSize(new Dimension(10, 50));
        //toolBarSpeedSlider.setMaximumSize(new Dimension(300, 31));

        //panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        //panel2.setMinimumSize(new Dimension(70, 10));
        //panel2.setPreferredSize(new Dimension(206, 10));
    }

    public void setParent(MainWindow parent) {
        this.parent = parent;
        playerChanged(parent.getGraphPlayer());
    }

    // region FROM TOOLBAR TO PLAYER

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphPlayer graphPlayer = parent.getGraphPlayer();
        Object eSource = e.getSource();
        if (eSource == toolBarSetReverseCheckBox) {
            JCheckBox currentCheck = (JCheckBox) eSource;
            parent.setImmediateReverse(currentCheck.isSelected());
        } else if (eSource == toolBarAutoButton) {
            //startVisualizing();
        } else if (eSource == toolBarPlayButton) {
            //if (graphPlayer.getState() == GraphPlayer.State.Empty) startVisualizing();
            toolBarSetReverseCheckBox.setEnabled(false);
            toolBarSpeedSlider.setEnabled(false);
            graphPlayer.setState(Play);
        } else if (eSource == toolBarPauseButton) {
            graphPlayer.setState(Pause);
            toolBarSpeedSlider.setEnabled(true);
        } else if (eSource == toolBarStepBackwardButton) {
            graphPlayer.stepBackward();
        } else if (eSource == toolBarStepForwardButton) {
            graphPlayer.stepForward();
        } else if (eSource == toolBarStopButton) {
            if (graphPlayer.getState() == Stop) parent.clearLog();
            else {
                toolBarSetReverseCheckBox.setEnabled(true);
                toolBarSpeedSlider.setEnabled(true);
                graphPlayer.setState(Stop);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        GraphPlayer graphPlayer = parent.getGraphPlayer();
        JSlider slider = (JSlider) e.getSource();
        if (slider == toolBarSpeedSlider) {
            graphPlayer.setDelay(slider.getValue());
        }
    }

    // endregion

    // region FROM PLAYER TO TOOLBAR

    public void playerChanged(GraphPlayer graphPlayer) {
        if (graphPlayer.sliderChanged) {
            graphPlayer.sliderChanged = false;
            toolBarSpeedSlider.setValue(graphPlayer.getDelay());
            return;
        }
        toolBarSetReverseCheckBox.setVisible(true);
        toolBarPlayButton.setVisible(graphPlayer.getState() != Play);
        toolBarPauseButton.setVisible(graphPlayer.getState() == Play);
        toolBarStepBackwardButton.setEnabled(graphPlayer.getState() != Stop);
        toolBarStepForwardButton.setEnabled(graphPlayer.getState() != Stop);
        toolBarSpeedSlider.setValue(graphPlayer.getDelay());
        toolBarProgressBar.setMaximum(graphPlayer.getFrameList() != null ? graphPlayer.getFrameList().count() - 1 : 0);
        toolBarProgressBar.setValue(graphPlayer.getCurrentFrame());

        switch (graphPlayer.getState()) {
            case Play:
                parent.playerPlaying();
                if (graphPlayer.getFrameList() != null) {
                    parent.playerVisualizing(graphPlayer.getFrameList().get(graphPlayer.getCurrentFrame()));
                }
                break;
            case Pause:
                parent.playerPausing();
                if (graphPlayer.getFrameList() != null) {
                    //костыль чтобы на паузе не дублировался лог
                    Graph graph = graphPlayer.getFrameList().get(graphPlayer.getCurrentFrame());
                    if (graphPlayer.stepBackwardInPause || graphPlayer.stepForwardInPause) {
                        graphPlayer.stepForwardInPause = false;
                        graphPlayer.stepBackwardInPause = false;
                    } else {
                        graph.state = "";
                    }
                    parent.playerVisualizing(graph);
                }
                break;
            case Stop:
                parent.playerStopping();
                break;
        }
    }

    // endregion
}
