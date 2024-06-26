package detector.items;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cnuphys.bCNU.util.Fonts;

public class AlphaSliderImp2 extends JPanel implements ChangeListener {

	// owner panel
	private final CedPanel3DImp2 _panel3D;

	// the slider
	private JSlider _slider;
	private static final int SLIDERWIDTH = 140;

	public AlphaSliderImp2(CedPanel3DImp2 panel3D, String prompt) {
		_panel3D = panel3D;

		setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));

		JLabel label = new JLabel(prompt);
		label.setFont(Fonts.smallFont);
		add(label);
		addSlider();
	}

	private void addSlider() {
		_slider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, _panel3D.init_alpha);
		_slider.setMajorTickSpacing(50);
		_slider.setPaintTicks(true);
		_slider.setPaintLabels(true);
		_slider.setFont(Fonts.tinyFont);
		_slider.setFocusable(false); // so ugly focus border not drawn

		_slider.addChangeListener(this);
		Dimension d = _slider.getPreferredSize();
		d.width = SLIDERWIDTH;
		_slider.setPreferredSize(d);

		add(_slider);
	}

	public JSlider getSlider() {
		return _slider;
	}
	
	public int getAlpha() {
		if (_slider == null) {
			return _panel3D.init_alpha;
		} else {
			return _slider.getValue();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		_panel3D.refresh();
	}
}
