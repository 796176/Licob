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


package constants;

import java.awt.*;

public class Colors {
	public static int LAYER_TRANSITION = 0x060606;
	public static Color LAYER0 = new Color(0x4e4e4e);
	public static Color LAYER1 = new Color(LAYER0.getRGB() - LAYER_TRANSITION);
	public static Color LAYER2 = new Color(LAYER1.getRGB() - LAYER_TRANSITION);
	public static Color LAYER3 = new Color(LAYER2.getRGB() - LAYER_TRANSITION);
	public static Color LAYER4 = new Color(LAYER3.getRGB() - LAYER_TRANSITION);
	public static Color LAYER5 = new Color(LAYER4.getRGB() - LAYER_TRANSITION);
	public static Color LAST_LAYER = LAYER5;
	public static Color FONT_COLOR = new Color(0xf8f8f8);
	public static Color MAIN_TEXT_FIELD_COLOR = Color.black;
	public static Color ADD_BUTTON_COLOR = new Color(0x1b830e);
	public static Color RUN_BUTTON_COLOR = new Color(0x1dbd1d);
	public static Color DELETE_BUTTON_COLOR = new Color(0xb30303);
	public static Color SCRIPT_AREA_COLOR = new Color(0x1d1d1d);
	public static Color SCRIPT_AREA_COLOR_INACTIVE = new Color(0x636363);
	public static Color SCRIPT_AREA_FONT_COLOR_INACTIVE = new Color(0xB0B0B0);
}
