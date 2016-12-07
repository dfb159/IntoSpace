package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {

	public static RawModel loadObjModel(String fileName, Loader loader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not load file! " + fileName + ".obj");
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<int[]> indices = new ArrayList<int[]>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] texturesArray = null;
		int[] indicesArray = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")) {
					vertices.add(new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3])));
				} else if (line.startsWith("vt ")) {
					textures.add(new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2])));
				} else if (line.startsWith("vn ")) {
					normals.add(new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3])));
				} else if (line.startsWith("f ")) {
					String[] vertex1 = currentLine[1].split("/");
					String[] vertex2 = currentLine[2].split("/");
					String[] vertex3 = currentLine[3].split("/");

					indices.add(new int[] { Integer.parseInt(vertex1[0]),
							Integer.parseInt(vertex1[1]),
							Integer.parseInt(vertex1[2]) });
					indices.add(new int[] { Integer.parseInt(vertex2[0]),
							Integer.parseInt(vertex2[1]),
							Integer.parseInt(vertex2[2]) });
					indices.add(new int[] { Integer.parseInt(vertex3[0]),
							Integer.parseInt(vertex3[1]),
							Integer.parseInt(vertex3[2]) });
				}
			}

			verticesArray = new float[vertices.size() * 3];
			int i = 0;
			for (Vector3f v : vertices) {
				verticesArray[i++] = v.x;
				verticesArray[i++] = v.y;
				verticesArray[i++] = v.z;
			}
			normalsArray = new float[indices.size() * 3];
			texturesArray = new float[indices.size() * 2];
			indicesArray = new int[indices.size()];

			i = 0;
			for (int[] index : indices) {
				int currentIndex = index[0] - 1;
				Vector2f tex = textures.get(index[1] - 1);
				Vector3f nor = normals.get(index[2] - 1);

				if (currentIndex * 3 >= normalsArray.length
						|| currentIndex * 2 >= texturesArray.length
						|| currentIndex >= indicesArray.length) {
					System.out.println("Corrupt file");
				}

				indicesArray[i++] = currentIndex;
				texturesArray[currentIndex * 2] = tex.x;
				texturesArray[currentIndex * 2 + 1] = 1 - tex.y;
				normalsArray[currentIndex * 3] = nor.x;
				normalsArray[currentIndex * 3 + 1] = nor.y;
				normalsArray[currentIndex * 3 + 2] = nor.z;
			}
		} catch (Exception e) {
			System.err.println("Error while reading file " + fileName + ".obj");
			e.printStackTrace();
		}

		return loader.loadToVAO(verticesArray, texturesArray, normalsArray,
				indicesArray);
	}
	
	public static RawModel loadObjModel(String fileName) {
		return loadObjModel(fileName, DisplayManager.getLoader());
	}

}
