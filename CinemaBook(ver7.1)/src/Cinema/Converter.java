package Cinema;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;
public final class Converter {
	private static final Converter converter = new Converter();
	public static Converter getInstance(){
		return converter;
	}
	
	public BufferedImage resizeImage(BufferedImage image, int width, int height) { //BufferedImage���g��܂��͏k�����ĕԂ�
		BufferedImage resizeImage = null;
		if(image != null || width > -1 || height > -1){
			resizeImage = new BufferedImage(width, height, image.getType());
			resizeImage.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH),
					0, 0, width, height, null);
		}
		return resizeImage;
	}
	
	public String converetImageToString(BufferedImage image) { //BufferedImage��String�ɕϊ����ĕԂ�
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] byteArray = null;
		try{
			ImageIO.write(image, "png", outputStream);
			outputStream.flush();
			byteArray = outputStream.toByteArray();
			outputStream.close();
		} catch(IOException ioException) {
			ioException.printStackTrace();
		}
		Base64.Encoder encoder = Base64.getEncoder();
		String imageString = new String(encoder.encodeToString(byteArray));
				
		return imageString;
	}
	
	public BufferedImage convertStringToImage(String imageString) { //String��BufferedImage�ɕϊ����ĕԂ�
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] imageByteArray = decoder.decode(imageString);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByteArray);
		BufferedImage image = null;
		try{
			image = ImageIO.read(inputStream);
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
		return image;
	}
}