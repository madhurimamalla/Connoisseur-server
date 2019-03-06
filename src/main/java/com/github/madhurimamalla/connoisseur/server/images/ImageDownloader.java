package com.github.madhurimamalla.connoisseur.server.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;

/**
 * Note: Available sizes also include w185
 * @author reema
 *
 */
public class ImageDownloader {

	private MovieService movieService;

	public ImageDownloader(MovieService movieService) {
		this.movieService = movieService;
	}

	public void downloadImages(long startId, long endId) {

		for (long i = startId; i <= endId; i++) {
			/**
			 * Fetch movie from movieService and then run the downloadImage
			 */
			Movie m = movieService.findMovieById(startId);
			if (m != null) {
				String imageURL = "http://image.tmdb.org/t/p/original/" + m.getTmdbPosterPath();
				try {
					downloadImage(imageURL, m.getTmdbMovieID());
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
		}

	}

	public void downloadImage(String imageURL, long movieId) throws IOException {

		final String IMAGES_DIR = System.getProperty("connoisseur.images.dir");

		File directory = new File(IMAGES_DIR);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		URL url = new URL(imageURL);

		String fileName = url.getFile();
		/**
		 * Creating directories
		 */
		File file = new File(IMAGES_DIR + "/" + Long.toString(movieId));
		file.mkdirs();
		/**
		 * Creating the file
		 */
		file.createNewFile();

		String destName = IMAGES_DIR + "/" + Long.toString(movieId) + fileName.substring(fileName.lastIndexOf("/"));
		System.out.println(destName);

		InputStream input = url.openStream();
		OutputStream output = new FileOutputStream(destName);

		byte[] b = new byte[2048];
		int length;

		while ((length = input.read(b)) != -1) {
			output.write(b, 0, length);
		}

		input.close();
		output.close();
	}

}
