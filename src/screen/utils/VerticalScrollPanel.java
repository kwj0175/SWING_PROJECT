package src.screen.utils;

import javax.swing.*;
import java.awt.*;

public class VerticalScrollPanel extends JPanel implements Scrollable {

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 64;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        // 뷰포트의 가로 크기를 따라가도록
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        // 세로는 내용만큼 커지고, 부족한 부분만 스크롤
        return false;
    }
}
