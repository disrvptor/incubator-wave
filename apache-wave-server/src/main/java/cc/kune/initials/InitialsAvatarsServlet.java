/*
 *
 * Copyright (C) 2007-2013 Licensed to the Comunes Association (CA) under
 * one or more contributor license agreements (see COPYRIGHT for details).
 * The CA licenses this file to you under the GNU Affero General Public
 * License version 3, (the "License"); you may not use this file except in
 * compliance with the License. This file is part of kune.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package cc.kune.initials;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class InitialsAvatarsServlet extends HttpServlet {

  private static final int MAX_IMG_SIZE = 1000;

  // http://txt2re.com/index-java.php3?s=111x111/aba_12ca&5&-19&6&-28&2
  private static final Pattern PARAMS = Pattern.compile("(\\d+)" + "(x)" + "(\\d+)" + "(\\/)"
      + "([\\w@\\.\\-]+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.UNICODE_CHARACTER_CLASS);

  private static final long serialVersionUID = 3757314092763168153L;

  private LoadingCache<String, Color> colorsCache;

  public InitialsAvatarsServlet() {
    // We'll cache the color of some user for some minutes (wo in a document, it
    // should show the same color)
    // https://code.google.com/p/guava-libraries/wiki/CachesExplained
    colorsCache = CacheBuilder.newBuilder().maximumSize(500).expireAfterAccess(
        InitialsConstants.CACHE_EXP_IN_SECS, TimeUnit.SECONDS).build(new CacheLoader<String, Color>() {
      @Override
      public Color load(final String key) {
        return new Color(ColorHelper.getRandomClearColorInt(key));
      }
    });
  }

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse resp)
      throws ServletException, IOException {

    final String path = request.getPathInfo().substring(1);

    final Matcher m = PARAMS.matcher(path);
    if (m.find()) {
      final int width = Math.min(Integer.parseInt(m.group(1)), MAX_IMG_SIZE);
      final int height = Math.min(Integer.parseInt(m.group(3)), MAX_IMG_SIZE);
      final String name = m.group(5);
      final String initial = name.substring(0, 1).toUpperCase();

      final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      final Graphics2D g2 = img.createGraphics();

      // Rectangle of random color
      final Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 0, width, height);
      g2.setPaint(colorsCache.getUnchecked(name));
      g2.fill(rectangle);

      // Antialiassing
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // Font scale
      final int fontSize = Math.round((Math.min(width, height) * InitialsConstants.BIG_FONT_FACTOR));
      final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
      g2.setFont(font);
      final FontMetrics fm = g2.getFontMetrics();
      final float fontSizeLast = width / (float) fm.stringWidth(initial) * fontSize;
      font.deriveFont(fontSizeLast);

      // Font color
      g2.setColor(Color.WHITE);

      // Center font
      final Rectangle2D fontRect = fm.getStringBounds(initial, g2);
      final int x = (width - (int) fontRect.getWidth()) / 2;
      final int y = (height - (int) fontRect.getHeight()) / 2 + fm.getAscent();

      // Draw font
      g2.drawString(initial, x, y);

      ImageIO.write(img, "PNG", resp.getOutputStream());
    } else {
      // Wrong params
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}