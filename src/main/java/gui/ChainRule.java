/* Licob - Licob Is a Chain-Oriented Backup
 * Copyright (C) 2024 Yegore Vlussove
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package gui;

import static java.util.Objects.requireNonNullElse;

public class ChainRule implements Comparable<ChainRule> {
	public String type;
	public String source;
	public String destination;
	public String exceptions;

	public ChainRule(String type, String source, String destination, String exceptions) {
		this.type = type;
		this.source = source;
		this.destination = destination;
		this.exceptions = exceptions;
	}

	public ChainRule(ChainItem item) {
		type = item.getType();
		source = item.getSource();
		destination = item.getDestination();
		exceptions = item.getExceptions();
	}

	public ChainRule() {}

	@Override
	public int compareTo(ChainRule rule) {
		if (type.compareTo(rule.type) != 0) return type.compareTo(rule.type);
		else if (requireNonNullElse(source, "").compareTo(requireNonNullElse(rule.source, "")) != 0)
			return requireNonNullElse(source, "").compareTo(requireNonNullElse(rule.source, ""));
		else if (requireNonNullElse(destination, "").compareTo(requireNonNullElse(rule.destination, "")) != 0)
			return requireNonNullElse(destination, "").compareTo(requireNonNullElse(rule.destination, ""));
		else if (exceptions.compareTo(rule.exceptions) != 0) return exceptions.compareTo(rule.exceptions);
		else return 0;
	}
}
