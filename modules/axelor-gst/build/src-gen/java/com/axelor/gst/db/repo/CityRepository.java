/*
 * Axelor Business Solutions
 * 
 * Copyright (C) 2005-2020 Axelor (<http://axelor.com>).
 * 
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.gst.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.gst.db.City;

public class CityRepository extends JpaRepository<City> {

	public CityRepository() {
		super(City.class);
	}

	public City findByName(String name) {
		return Query.of(City.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

}

