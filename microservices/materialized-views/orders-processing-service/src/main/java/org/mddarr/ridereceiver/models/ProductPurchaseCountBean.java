/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mddarr.ridereceiver.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author Soby Chacko
 */

@AllArgsConstructor
@NoArgsConstructor
public class ProductPurchaseCountBean {

	private String vendor;
	private String product;
	private Long purchases;

	@Override
	public String toString() {
		return "SongPlayCountBean{" +
				"vendor='" + vendor + '\'' +
				", product='" + product + '\'' +
				", purchases=" + purchases +
				'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final ProductPurchaseCountBean that = (ProductPurchaseCountBean) o;
		return Objects.equals(vendor, that.vendor) &&
				Objects.equals(product, that.product) &&
				Objects.equals(purchases, that.purchases);
	}

	@Override
	public int hashCode() {
		return Objects.hash(vendor, product, purchases);
	}
}
