package bCNU3D;

import java.nio.FloatBuffer;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
/*
 * Copyright (c) 2009-2021 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.TransparentComparator;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer.Type;

public class NewTransparentComparator extends TransparentComparator {

	private Camera cam;
	private final Vector3f tempVec = new Vector3f();

	@Override
	public void setCamera(Camera cam) {
		this.cam = cam;
	}

	private float distanceToCam(Geometry spat) {
		// NOTE: It is best to check the distance
		// to the bound's closest edge vs. the bound's center here.
		float dist = spat.getWorldTranslation().distance(cam.getLocation());
		// Vector3f directionToCenter =
		// (spat.getWorldTranslation().subtract(cam.getLocation())).divide(spat.getWorldTranslation().distance(cam.getLocation()));
		Vector3f directionToPoint = cam.getDirection();

		CollisionResults results_piece = new CollisionResults();
		Ray ray_piece = new Ray(cam.getLocation(), directionToPoint.mult(dist));
		spat.collideWith(ray_piece, results_piece);
		if (results_piece.getClosestCollision() != null) {
			results_piece.getClosestCollision().getContactPoint();
			if (spat.getName() == "Cube1") {
				// System.out.println("Cube1 dist: " + loc.distance(cam.getLocation()));
			} else if (spat.getName() == "TriangleS") {
				// System.out.println("TriangleS dist: " + loc.distance(cam.getLocation()));
			}
			// return loc.distance(cam.getLocation());
			return spat.getWorldBound().distanceToEdge(cam.getLocation());
		} else {
			if (spat.getName() == "TriangleS") {
				// System.out.println("TriangleS dist: " +
				// spat.getWorldBound().distanceToEdge(cam.getLocation()));
			}
			return spat.getWorldBound().distanceToEdge(cam.getLocation());
		}
		// draw a line the same length from a to b
		// start at cam position
		// follow direction until hitting object
		// intersection position is the correct one
	}

	@Override
	public int compare(Geometry o1, Geometry o2) {
		//System.out.println(o2.getName() + " " + o1.getName());
		//System.out.println(o1.getName() == "Cube1" && o2.getName() == "Cube2");
		if (o1.getParent().getName() == "OverlappingTransparent" && o2.getName() == "OverlappingTransparent") {
			
			FloatBuffer pos = (FloatBuffer) (o2.getMesh().getBuffer(Type.Position).getData());
			pos.rewind();
			int i = 0;
			boolean rayCollidedO1 = false;
			while (pos.hasRemaining()) {
				float x = pos.get();
				float y = pos.get();
				float z = pos.get();
				Vector3f temp = new Vector3f(x, y, z);
				temp = o2.getWorldTransform().transformVector(temp, null);
				Vector3f direction = (temp.subtract(cam.getLocation())).normalize();
				CollisionResults results_piece = new CollisionResults();
				Ray ray_piece = new Ray(cam.getLocation(), direction);
				o1.collideWith(ray_piece, results_piece);
				if (results_piece.getClosestCollision() != null) {
					rayCollidedO1 = true;
					float o2point = results_piece.getClosestCollision().getContactPoint().distance(cam.getLocation());
					float o1point = temp.distance(cam.getLocation());
					System.out.println("Dist to edge " + i + " " + o1point);
					System.out.println("Dist to cube " + o2point);
					
					if (o1point > o2point) {
						/*try {
						o1.getParent().getChild("Line").removeFromParent();
						}catch(Exception e) {
							
						}
						Geometry line = ShapeGenerator.createLine(temp, results_piece.getClosestCollision().getContactPoint(), ColorRGBA.Black);
						line.setName("Line");
						o1.getParent().attachChild(line);*/
						System.out.println(i + " TRIANGLE EDGE NOT VISIBLE!");
						return 1;
					}
						
				}
				
				i++;

			}
			
			FloatBuffer pos2 = (FloatBuffer) (o1.getMesh().getBuffer(Type.Position).getData());
			pos2.rewind();
			while (pos2.hasRemaining()) {
				float x = pos2.get();
				float y = pos2.get();
				float z = pos2.get();
				Vector3f temp = new Vector3f(x, y, z);
				temp = o1.getWorldTransform().transformVector(temp, null);
				Vector3f direction = (temp.subtract(cam.getLocation())).normalize();
				CollisionResults results_piece = new CollisionResults();
				Ray ray_piece = new Ray(cam.getLocation(), direction);
				o2.collideWith(ray_piece, results_piece);
				if (results_piece.getClosestCollision() != null) {
					float o2point = results_piece.getClosestCollision().getContactPoint().distance(cam.getLocation());
					float o1point = temp.distance(cam.getLocation());
					
					if (o1point > o2point) {
						/*try {
							o1.getParent().getChild("Line").removeFromParent();
							}catch(Exception e) {
								
							}
						Geometry line = ShapeGenerator.createLine(temp, results_piece.getClosestCollision().getContactPoint(), ColorRGBA.Red);
						line.setName("Line");
						*/
						System.out.println(i + " CUBE EDGE NOT VISIBLE!");
						return -1;
					}
						
				}
				

			}
			
			if(rayCollidedO1)
				return -1;
			else
				return 1;
			
		}

		float d1 = distanceToCam(o1);
		float d2 = distanceToCam(o2);
		if (d1 == d2)
			return 0;
		else if (d1 < d2)
			return 1;
		else
			return -1;

	}
}