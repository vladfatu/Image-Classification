/*
LBPDemo - Demo implementation for using LBPModel
(c) 2011 Florian Brucker, http://www.florianbrucker.de

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.imageclassification.lbp;

import java.io.*;

/**
 * A simple demo for using the {@link LBPModel} to automatically classify textures.
 * <p>
 * This class implements a very simple command line interface application which can be
 * used to train a classifier and classify textures. It supports two operations:
 * <p>
 * <i>Training:</i> In this mode, a model is created from a set of images and stored in a file.
 * <p>
 * <i>Classification:</i> In this mode, a model is created from a single image, and this model
 * is then classified using a set of models built previously using the training command. 
 */
public class LBPDemo {
	
	/**
	 * Prints usage information and an error message to STDOUT.
	 * 
	 * @param s Error message
	 */
	public static void printUsage(String s) {
		System.out.println("Locally Binary Pattern Classifier Demo");
		System.out.println("(c) 2011 Florian Brucker");
		System.out.println("");
		System.out.println("Error: " + s);
		System.out.println("Usage:");
		System.out.println(" lbpdemo -t modelfile imagefiles");
		System.out.println(" lbpdemo -c modelfiles imagefile");		
		System.exit(1);
	}
	
	/**
	 * Parses file names from the list of command line arguments.
	 * 
	 * @param args Command line arguments
	 * @param start Index to start parsing (including)
	 * @param end Index to stop parsing (excluding)
	 * @return The corresponding files
	 */
	public static File[] parseFiles(String[] args, int start, int end) {
		File files[] = new File[end - start];
		for (int i = start; i < end; i++) {
			files[i - start] = new File(args[i]);
		}
		return files;
	}

	
	/**
	 * Main program logic.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {	
	
		if (args.length < 3) {			
			printUsage("Not enough arguments");
		}		
		
		int cmd;		
		if (args[0].equals("-t")) {
			// Train
			File modelFile = new File(args[1]);
			File imageFiles[] = parseFiles(args, 2, args.length);
			
			LBPParameters p = new LBPParameters(
				new int[] {8, 16, 24}, 
				new int[] { 1, 2, 3},
				new int[] { 10, 10, 10}
			);
			
			LBPModel model = null;
			try {
				model = new LBPModel(p, imageFiles);
			} catch (IOException e) {
				System.err.println("Could not load images: " + e.getMessage());
				System.exit(1);
			}
			try {
				model.save(modelFile);
			} catch (IOException e) {
				System.err.println("Could not store model: " + e.getMessage());
				System.exit(1);
			}
			
		} else if (args[0].equals("-c")) {
			// Classify
			File modelFiles[] = parseFiles(args, 1, args.length - 1);
			File imageFile = new File(args[args.length - 1]);
			
			LBPModel models[] = new LBPModel[modelFiles.length];
			for (int i = 0; i < models.length; i++) {
				try {
					models[i] = new LBPModel(modelFiles[i]);
				} catch (IOException e) {
					System.err.println("Could not load model: " + e.getMessage());
					System.exit(1);
				}
			}
			
			LBPParameters p = models[0].getParameters();
			
			LBPModel sample = null;
			try {
				sample = new LBPModel(p, imageFile);
			} catch (IOException e) {
				System.err.println("Could not load image: " + e.getMessage());
				System.exit(1);
			}
			int best = 0;
			float maxgof = Float.NEGATIVE_INFINITY;
			for (int i = 0; i < models.length; i++) {
				float gof = sample.goodnessOfFit(models[i]);
				System.out.println(modelFiles[i].getName() + ": " + gof);
				if (gof > maxgof) {
					maxgof = gof;
					best = i;
				}
			}
			System.out.println("=> Classified as " + modelFiles[best].getName());
			
		} else {
			printUsage("Unknown Command '" + args[0] + "'");
		}
		
	}

}
