package com.mapofzones.tokenmatcher.utils;

import java.net.URI;

public class UriHelper {

	public static URI modifyUri(URI uri) {
		if (uri.toString().contains("--rpc--")) {
			uri = URI.create(uri.toString().replace("--rpc--", "--lcd--"));
//		} else if (uri.getPort() == 26657) {
//			uri = URI.create(uri.toString().replace("26657", "1317"));
//		} else if (uri.getPort() == 26659) {
//			uri = URI.create(uri.toString().replace("26659", "1317"));
		} else if (uri.toString().contains("https://rpc.sifchain.finance:443")) {
			uri = URI.create(uri.toString().replace("https://rpc.sifchain.finance:443", "https://api.sifchain.finance"));
		} else uri.resolve("");
		return uri;
	}
}