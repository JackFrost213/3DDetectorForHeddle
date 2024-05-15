package detector.items;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.event.ChangeEvent;

import bCNU3D.Support3DOther;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.frame.CedColors;
import cnuphys.lund.LundId;
import cnuphys.lund.LundStyle;
import cnuphys.swim.SwimMenu;
import cnuphys.swim.SwimTrajectory;
import cnuphys.swim.Swimming;

import com.jme3.scene.Node;
import shapes3D.Item3DImp2;

public class TrajectoryDrawer3DImp2 extends Item3DImp2 {

	private CedPanel3DImp2 _cedPanel3D;
	private List<SwimTrajectory> previous_trajectories = new ArrayList<SwimTrajectory>();
	private List<SwimTrajectory> previous_recon_trajectories = new ArrayList<SwimTrajectory>();

	public TrajectoryDrawer3DImp2(Node root, CedPanel3DImp2 panel3D) {
		super(panel3D);
		_cedPanel3D = panel3D;
		_parentNode = new Node();

		// Do some initialization of geometries
		root.attachChild(_parentNode);
	}

	// draw a trajectory in 3D
	private void drawSwimTrajectory(Node root, SwimTrajectory traj, Color color) {
		int size = traj.size();
		if (size < 2) {
			return;
		}

		float coords[] = new float[3 * size];

		if (color == null) {
			LundId lid = traj.getLundId();
			LundStyle style = LundStyle.getStyle(lid);

			color = Color.black;

			if (style != null) {
				color = style.getFillColor();
			}
		}

		for (int i = 0; i < size; i++) {
			double v[] = traj.get(i);
			int j = i * 3;
			// convert to cm
			coords[j] = 100 * (float) v[0];
			coords[j + 1] = 100 * (float) v[1];
			coords[j + 2] = 100 * (float) v[2];
		}

		Support3DOther.drawPolyLine(root, coords, color, 2f);
	}

	@Override
	public void update(float tpf) {
		if (ClasIoEventManager.getInstance().isAccumulating()) {
			return;
		}
		// mc tracks
		if (SwimMenu.getInstance().showMonteCarloTracks()) {
			List<SwimTrajectory> trajectories = Swimming.getMCTrajectories();
			if (trajectories != null && (!(trajectories.equals(previous_trajectories)))) {
				_parentNode.detachAllChildren();
				previous_trajectories = new ArrayList<SwimTrajectory>();
				try {
					for (SwimTrajectory trajectory : trajectories) {
						previous_trajectories.add(trajectory);
						drawSwimTrajectory(_parentNode, trajectory, null);
					}
				} catch (ConcurrentModificationException e) {
					 System.out.println(e.toString() + " :Modified Trajectory while drawing Trajectory");
				}

			} else if (trajectories.equals(new ArrayList<SwimTrajectory>())
					&& !previous_trajectories.equals(new ArrayList<SwimTrajectory>())) {
				_parentNode.detachAllChildren();
				previous_trajectories = new ArrayList<SwimTrajectory>();
			}
		}

		// reconstructed?
		if (SwimMenu.getInstance().showReconstructedTracks()) {
			List<SwimTrajectory> trajectories = Swimming.getReconTrajectories();
			if (trajectories != null && (!(trajectories.equals(previous_recon_trajectories)))) {
				_parentNode.detachAllChildren();
				previous_recon_trajectories = new ArrayList<SwimTrajectory>();
				try {
					for (SwimTrajectory trajectory : trajectories) {
						previous_recon_trajectories.add(trajectory);
						boolean show = false;
						Color color = null;

						String source = trajectory.getSource();
						if (source != null) {
							if (source.contains("HitBasedTrkg::HBTracks")) {
								show = _cedPanel3D.showHBTrack();
								color = CedColors.HB_COLOR;
							} else if (source.contains("TimeBasedTrkg::TBTracks")) {
								show = _cedPanel3D.showTBTrack();
								color = CedColors.TB_COLOR;
							} else if (source.contains("HitBasedTrkg::AITracks")) {
								show = _cedPanel3D.showAIHBTrack();
								color = CedColors.AIHB_COLOR;
							} else if (source.contains("TimeBasedTrkg::AITracks")) {
								show = _cedPanel3D.showAITBTrack();
								color = CedColors.AITB_COLOR;
							} else if (source.contains("REC::Particle")) {
								show = _cedPanel3D.showRecTrack();
								color = Color.darkGray;
							} else if (source.contains("CVTRec::Tracks")) {
								show = _cedPanel3D.showCVTTrack();
								color = CedColors.CVT_COLOR;
							}
							
						}

						LundId lid = trajectory.getLundId();

						if (lid != null) {
							color = lid.getStyle().getLineColor();
						}
						if (show) {
							drawSwimTrajectory(_parentNode, trajectory, color);
						}
					}
				} catch (ConcurrentModificationException e) {
					System.out.println(e.toString() + " :Modified Trajectory while drawing Trajectory");
				}

			} else if (trajectories.equals(new ArrayList<SwimTrajectory>())
					&& !previous_recon_trajectories.equals(new ArrayList<SwimTrajectory>())) {
				_parentNode.detachAllChildren();
				previous_recon_trajectories = new ArrayList<SwimTrajectory>();
			}
		}
	}

	@Override
	public void handleSliderInterrupt(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCheckBoxInterrupt(ActionEvent e) {
		AbstractButton o = (AbstractButton) e.getSource();
		String name = o.getText();
		if (name.contains("Track")) {
			previous_recon_trajectories = new ArrayList<SwimTrajectory>();
			previous_trajectories = new ArrayList<SwimTrajectory>();
		}

	}
}
