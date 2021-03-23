package br.com.ottimizza.integradorcloud.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.AnalyzeDocumentRequest;
import software.amazon.awssdk.services.textract.model.AnalyzeDocumentResponse;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.FeatureType;

@Service
public class AmazonService {

	@Value("${storage-s3.service.bucket}")
	private String STORAGE_BUCKET;
	
	public String analizarArquivo(String nomeArquivo) throws Exception {

		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withEndpointConfiguration(new EndpointConfiguration("https://s3.amazonaws.com", "sa-east-1")).build();
		S3Object s3Object = s3client.getObject(STORAGE_BUCKET,nomeArquivo);
		S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

		SdkBytes bytes = SdkBytes.fromInputStream(s3ObjectInputStream);
		Document doc = Document.builder().bytes(bytes).build();

		List<FeatureType> list = new ArrayList<FeatureType>();
		list.add(FeatureType.FORMS);

		AnalyzeDocumentRequest request = AnalyzeDocumentRequest.builder().featureTypes(list).document(doc).build();

		TextractClient textractClient = TextractClient.builder().region(Region.CA_CENTRAL_1).build();

		AnalyzeDocumentResponse response = textractClient.analyzeDocument(request);
		List<Block> blocks = response.blocks();

		StringBuilder sb = new StringBuilder();
		String linhaAnterior = "";
		for (Block b : blocks) {
			if (b.blockType().equals(BlockType.LINE)) {
				String texto = b.text();
				float linhaFloat = b.geometry().boundingBox().top();
				String linha = String.valueOf(linhaFloat).substring(0, 4);
				if (!linhaAnterior.equals(linha) && !linhaAnterior.equals("")) {
					sb.append("\r\n");
				}
				sb.append(texto + " ");
				linhaAnterior = linha;
			}
		}
		return sb.toString();
	}
	
}
