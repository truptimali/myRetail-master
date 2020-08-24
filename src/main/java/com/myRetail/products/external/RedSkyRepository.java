package com.myRetail.products.external;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.myRetail.products.exception.InterfaceException;

@Service
public class RedSkyRepository {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${host}")
	private String apiEndpointURL;

	// JSON extraction method from HTTP URL, to extract the title description from
	// external API
	public String getTitleFromExternalAPI(String id) throws InterfaceException {
		try {
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiEndpointURL+id).queryParam("excludes","taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");

			String externalAPIResult = restTemplate.getForObject(builder.build().encode().toUri(), String.class);
			String retrievedTitle = new JSONObject(externalAPIResult.toString()).getJSONObject("product")
					.getJSONObject("item").getJSONObject("product_description").getString("title");
			return retrievedTitle;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InterfaceException(HttpStatus.NOT_FOUND.value() ,"Product Remote API unavailable");
		}
	}

}
