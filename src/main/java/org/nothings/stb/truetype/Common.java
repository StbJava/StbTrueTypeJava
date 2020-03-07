package org.nothings.stb.truetype;

public class Common {
	public static final int STBTT_PLATFORM_ID_UNICODE = 0;
	public static final int STBTT_PLATFORM_ID_MAC = 1;
	public static final int STBTT_PLATFORM_ID_ISO = 2;
	public static final int STBTT_PLATFORM_ID_MICROSOFT = 3;

	public static final int STBTT_UNICODE_EID_UNICODE_1_0 = 0;
	public static final int STBTT_UNICODE_EID_UNICODE_1_1 = 1;
	public static final int STBTT_UNICODE_EID_ISO_10646 = 2;
	public static final int STBTT_UNICODE_EID_UNICODE_2_0_BMP = 3;
	public static final int STBTT_UNICODE_EID_UNICODE_2_0_FULL = 4;

	public static final int STBTT_MS_EID_SYMBOL = 0;
	public static final int STBTT_MS_EID_UNICODE_BMP = 1;
	public static final int STBTT_MS_EID_SHIFTJIS = 2;
	public static final int STBTT_MS_EID_UNICODE_FULL = 10;

	public static final int STBTT_MAC_EID_ROMAN = 0;
	public static final int STBTT_MAC_EID_ARABIC = 4;
	public static final int STBTT_MAC_EID_JAPANESE = 1;
	public static final int STBTT_MAC_EID_HEBREW = 5;
	public static final int STBTT_MAC_EID_CHINESE_TRAD = 2;
	public static final int STBTT_MAC_EID_GREEK = 6;
	public static final int STBTT_MAC_EID_KOREAN = 3;
	public static final int STBTT_MAC_EID_RUSSIAN = 7;

	public static final int STBTT_MS_LANG_ENGLISH = 0x0409;
	public static final int STBTT_MS_LANG_ITALIAN = 0x0410;
	public static final int STBTT_MS_LANG_CHINESE = 0x0804;
	public static final int STBTT_MS_LANG_JAPANESE = 0x0411;
	public static final int STBTT_MS_LANG_DUTCH = 0x0413;
	public static final int STBTT_MS_LANG_KOREAN = 0x0412;
	public static final int STBTT_MS_LANG_FRENCH = 0x040c;
	public static final int STBTT_MS_LANG_RUSSIAN = 0x0419;
	public static final int STBTT_MS_LANG_GERMAN = 0x0407;
	public static final int STBTT_MS_LANG_SPANISH = 0x0409;
	public static final int STBTT_MS_LANG_HEBREW = 0x040d;
	public static final int STBTT_MS_LANG_SWEDISH = 0x041D;

	public static final int STBTT_MAC_LANG_ENGLISH = 0;
	public static final int STBTT_MAC_LANG_JAPANESE = 11;
	public static final int STBTT_MAC_LANG_ARABIC = 12;
	public static final int STBTT_MAC_LANG_KOREAN = 23;
	public static final int STBTT_MAC_LANG_DUTCH = 4;
	public static final int STBTT_MAC_LANG_RUSSIAN = 32;
	public static final int STBTT_MAC_LANG_FRENCH = 1;
	public static final int STBTT_MAC_LANG_SPANISH = 6;
	public static final int STBTT_MAC_LANG_GERMAN = 2;
	public static final int STBTT_MAC_LANG_SWEDISH = 5;
	public static final int STBTT_MAC_LANG_HEBREW = 10;
	public static final int STBTT_MAC_LANG_CHINESE_SIMPLIFIED = 33;
	public static final int STBTT_MAC_LANG_ITALIAN = 3;
	public static final int STBTT_MAC_LANG_CHINESE_TRAD = 19;

	public static final short STBTT_vmove = 1;
	public static final short STBTT_vline = 2;
	public static final short STBTT_vcurve = 3;
	public static final short STBTT_vcubic = 4;

	public static long stbtt__find_table(FakePtrByte data, int fontstart, String tag) {
		int num_tables = ttUSHORT(data.cloneAdd(fontstart + 4));
		int tabledir = fontstart + 12;
		int i;
		for (i = 0; i < num_tables; ++i) {
			int loc = tabledir + 16 * i;

			FakePtrByte p = data.cloneAdd(loc);
			if (p.getAt(0) == tag.charAt(0) &&
					p.getAt(1) == tag.charAt(1) &&
					p.getAt(2) == tag.charAt(2) &&
					p.getAt(3) == tag.charAt(3))
				return ttULONG(data.cloneAdd(loc + 8));
		}

		return 0;
	}

	public static int ttUSHORT(FakePtrByte p) {
		return (int) (p.getAt(0) * 256 + p.getAt(1));
	}

	public static short ttSHORT(FakePtrByte p) {
		return (short) (p.getAt(0) * 256 + p.getAt(1));
	}

	public static long ttULONG(FakePtrByte p) {
		return (long) ((p.getAt(0) << 24) + (p.getAt(1) << 16) + (p.getAt(2) << 8) + p.getAt(3));
	}

	public static int ttLONG(FakePtrByte p) {
		return (p.getAt(0) << 24) + (p.getAt(1) << 16) + (p.getAt(2) << 8) + p.getAt(3);
	}

	public static int stbtt__isfont(FakePtrByte font) {
		if (font[0] == '1' && font[1] == 0 && font[2] == 0 && font[3] == 0)
			return 1;
		if (font[0] == "typ1"[0] && font[1] == "typ1"[1] && font[2] == "typ1"[2] && font[3] == "typ1"[3])
			return 1;
		if (font[0] == "OTTO"[0] && font[1] == "OTTO"[1] && font[2] == "OTTO"[2] && font[3] == "OTTO"[3])
			return 1;
		if (font[0] == 0 && font[1] == 1 && font[2] == 0 && font[3] == 0)
			return 1;
		if (font[0] == "true"[0] && font[1] == "true"[1] && font[2] == "true"[2] && font[3] == "true"[3])
			return 1;
		return 0;
	}

	public static int stbtt_GetFontOffsetForIndex_internal(FakePtrByte font_collection, int index) {
		if (stbtt__isfont(font_collection) != 0)
			return index == 0 ? 0 : -1;
		if (font_collection[0] == "ttcf"[0] && font_collection[1] == "ttcf"[1] && font_collection[2] == "ttcf"[2] &&
				font_collection[3] == "ttcf"[3])
			if (ttULONG(font_collection + 4) == 0x00010000 || ttULONG(font_collection + 4) == 0x00020000) {
				var n = ttLONG(font_collection + 8);
				if (index >= n)
					return -1;
				return (int) ttULONG(font_collection + 12 + index * 4);
			}

		return -1;
	}

	public static int stbtt_GetNumberOfFonts_internal(FakePtrByte font_collection) {
		if (stbtt__isfont(font_collection) != 0)
			return 1;
		if (font_collection[0] == "ttcf"[0] && font_collection[1] == "ttcf"[1] && font_collection[2] == "ttcf"[2] &&
				font_collection[3] == "ttcf"[3])
			if (ttULONG(font_collection + 4) == 0x00010000 || ttULONG(font_collection + 4) == 0x00020000)
				return ttLONG(font_collection + 8);

		return 0;
	}

	public static void stbtt_setvertex(stbtt_vertex v, short type, int x, int y, int cx, int cy) {
		v.type = type;
		v.x = (short) x;
		v.y = (short) y;
		v.cx = (short) cx;
		v.cy = (short) cy;
	}

	public static int stbtt__close_shape(stbtt_vertex[] vertices, int num_vertices, int was_off, int start_off,
										 int sx, int sy, int scx, int scy, int cx, int cy) {
		stbtt_vertex v = new stbtt_vertex();
		if (start_off != 0) {
			if (was_off != 0) {
				stbtt_setvertex(v, STBTT_vcurve, (cx + scx) >> 1, (cy + scy) >> 1, cx, cy);
				vertices[num_vertices++] = v;
			}

			stbtt_setvertex(v, STBTT_vcurve, sx, sy, scx, scy);
			vertices[num_vertices++] = v;
		} else {
			if (was_off != 0) {
				stbtt_setvertex(v, STBTT_vcurve, sx, sy, cx, cy);
				vertices[num_vertices++] = v;
			} else {
				stbtt_setvertex(v, STBTT_vline, sx, sy, 0, 0);
				vertices[num_vertices++] = v;
			}
		}

		return num_vertices;
	}

	public static int stbtt__GetCoverageIndex(FakePtrByte coverageTable, int glyph) {
		int coverageFormat = ttUSHORT(coverageTable);
		switch (coverageFormat) {
			case 1: {
				int glyphCount = ttUSHORT(coverageTable.cloneAdd(2));
				int l = 0;
				int r = glyphCount - 1;
				int m = 0;
				int straw = 0;
				int needle = glyph;
				while (l <= r) {
					FakePtrByte glyphArray = coverageTable.cloneAdd(4);
					int glyphID = 0;
					m = (l + r) >> 1;
					glyphID = ttUSHORT(glyphArray.cloneAdd(2 * m));
					straw = glyphID;
					if (needle < straw)
						r = m - 1;
					else if (needle > straw)
						l = m + 1;
					else
						return m;
				}
			}
			break;
			case 2: {
				int rangeCount = ttUSHORT(coverageTable.cloneAdd(2));
				FakePtrByte rangeArray = coverageTable.cloneAdd(4);
				int l = 0;
				int r = rangeCount - 1;
				int m = 0;
				int strawStart = 0;
				int strawEnd = 0;
				int needle = glyph;
				while (l <= r) {
					FakePtrByte rangeRecord;
					m = (l + r) >> 1;
					rangeRecord = rangeArray.cloneAdd(6 * m);
					strawStart = ttUSHORT(rangeRecord);
					strawEnd = ttUSHORT(rangeRecord.cloneAdd(2));
					if (needle < strawStart) {
						r = m - 1;
					} else if (needle > strawEnd) {
						l = m + 1;
					} else {
						int startCoverageIndex = ttUSHORT(rangeRecord.cloneAdd(4));
						return startCoverageIndex + glyph - strawStart;
					}
				}
			}
			break;
			default: {
			}
			break;
		}

		return -1;
	}

	public static int stbtt__GetGlyphClass(FakePtrByte classDefTable, int glyph) {
		int classDefFormat = ttUSHORT(classDefTable);
		switch (classDefFormat) {
			case 1: {
				int startGlyphID = ttUSHORT(classDefTable.cloneAdd(2));
				int glyphCount = ttUSHORT(classDefTable.cloneAdd(4));
				FakePtrByte classDef1ValueArray = classDefTable.cloneAdd(6);
				if (glyph >= startGlyphID && glyph < startGlyphID + glyphCount)
					return ttUSHORT(classDef1ValueArray.cloneAdd(2 * (glyph - startGlyphID)));
				classDefTable = classDef1ValueArray.cloneAdd(2 * glyphCount);
			}
			break;
			case 2: {
				int classRangeCount = ttUSHORT(classDefTable.cloneAdd(2));
				FakePtrByte classRangeRecords = classDefTable.cloneAdd(4);
				int l = 0;
				int r = classRangeCount - 1;
				int m = 0;
				int strawStart = 0;
				int strawEnd = 0;
				int needle = glyph;
				while (l <= r) {
					FakePtrByte classRangeRecord;
					m = (l + r) >> 1;
					classRangeRecord = classRangeRecords.cloneAdd(6 * m);
					strawStart = ttUSHORT(classRangeRecord);
					strawEnd = ttUSHORT(classRangeRecord.cloneAdd(2));
					if (needle < strawStart)
						r = m - 1;
					else if (needle > strawEnd)
						l = m + 1;
					else
						return ttUSHORT(classRangeRecord.cloneAdd(4));
				}

				classDefTable = classRangeRecords.cloneAdd(6 * classRangeCount);
			}
			break;
			default: {
			}
			break;
		}

		return -1;
	}

	public static stbtt__active_edge stbtt__new_active(stbtt__edge e, int off_x, float start_point) {
		stbtt__active_edge z = new stbtt__active_edge();
		float dxdy = (e.x1 - e.x0) / (e.y1 - e.y0);
		z.fdx = dxdy;
		z.fdy = dxdy != 0.0f ? 1.0f / dxdy : 0.0f;
		z.fx = e.x0 + dxdy * (start_point - e.y0);
		z.fx -= off_x;
		z.direction = e.invert != 0 ? 1.0f : -1.0f;
		z.sy = e.y0;
		z.ey = e.y1;
		z.next = null;
		return z;
	}

	public static int stbtt_BakeFontBitmap_internal(short[] data, int offset, float pixel_height,
													FakePtrByte pixels, int pw, int ph, int first_char, int num_chars, stbtt_bakedchar[] chardata) {
		float scale = 0;
		int x = 0;
		int y = 0;
		int bottom_y = 0;
		int i = 0;
		FontInfo f = new FontInfo();
		if (f.stbtt_InitFont(data, offset) == 0)
			return -1;
		pixels.memset(0, pw * ph);
		x = y = 1;
		bottom_y = 1;
		scale = f.stbtt_ScaleForPixelHeight(pixel_height);
		for (i = 0; i < num_chars; ++i) {
			int advance = 0;
			int lsb = 0;
			int x0 = 0;
			int y0 = 0;
			int x1 = 0;
			int y1 = 0;
			int gw = 0;
			int gh = 0;
			int g = f.stbtt_FindGlyphIndex(first_char + i);
			GlyphHMetrics hm = f.stbtt_GetGlyphHMetrics(g);
			advance = hm.AdvanceWidth;
			lsb = hm.LeftSideBearing;
			f.stbtt_GetGlyphBitmapBox(g, scale, scale, ref x0, ref y0, ref x1, ref y1);
			gw = x1 - x0;
			gh = y1 - y0;
			if (x + gw + 1 >= pw) {
				y = bottom_y;
				x = 1;
			}

			if (y + gh + 1 >= ph)
				return -i;
			f.stbtt_MakeGlyphBitmap(pixels + x + y * pw, gw, gh, pw, scale, scale, g);
			chardata[i].x0 = (int) (short) x;
			chardata[i].y0 = (int) (short) y;
			chardata[i].x1 = (int) (short) (x + gw);
			chardata[i].y1 = (int) (short) (y + gh);
			chardata[i].xadvance = scale * advance;
			chardata[i].xoff = x0;
			chardata[i].yoff = y0;
			x = x + gw + 1;
			if (y + gh + 1 > bottom_y)
				bottom_y = y + gh + 1;
		}

		return bottom_y;
	}

	public static void stbtt_GetBakedQuad(stbtt_bakedchar[] chardata, int pw, int ph, int char_index,
										  ref float xpos, ref float ypos, ref stbtt_aligned_quad q, int opengl_fillrule) {
		var d3d_bias = opengl_fillrule != 0 ? 0 : -0.5f;
		var ipw = 1.0f / pw;
		var iph = 1.0f / ph;
		int round_x = (int) Math.Floor(xpos + chardata[char_index].xoff + 0.5f);
		int round_y = (int) Math.Floor(ypos + chardata[char_index].yoff + 0.5f);
		q.x0 = round_x + d3d_bias;
		q.y0 = round_y + d3d_bias;
		q.x1 = round_x + chardata[char_index].x1 - chardata[char_index].x0 + d3d_bias;
		q.y1 = round_y + chardata[char_index].y1 - chardata[char_index].y0 + d3d_bias;
		q.s0 = chardata[char_index].x0 * ipw;
		q.t0 = chardata[char_index].y0 * iph;
		q.s1 = chardata[char_index].x1 * ipw;
		q.t1 = chardata[char_index].y1 * iph;
		xpos += chardata[char_index].xadvance;
	}

	public static void stbtt__h_prefilter(FakePtrByte pixels, int w, int h, int stride_in_shorts,
										  long kernel_width) {
		var buffer = new short[8];
		int safe_w = (int) (w - kernel_width);
		int j = 0;

		Array.Clear(buffer, 0, 8);
		for (j = 0; j < h; ++j) {
			int i = 0;
			long total = 0;
			Array.Clear(buffer, 0, (int) kernel_width);
			total = 0;
			switch (kernel_width) {
				case 2:
					for (i = 0; i <= safe_w; ++i) {
						total += (long) (pixels[i] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i];
						pixels[i] = (short) (total / 2);
					}

					break;
				case 3:
					for (i = 0; i <= safe_w; ++i) {
						total += (long) (pixels[i] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i];
						pixels[i] = (short) (total / 3);
					}

					break;
				case 4:
					for (i = 0; i <= safe_w; ++i) {
						total += (long) (pixels[i] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i];
						pixels[i] = (short) (total / 4);
					}

					break;
				case 5:
					for (i = 0; i <= safe_w; ++i) {
						total += (long) (pixels[i] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i];
						pixels[i] = (short) (total / 5);
					}

					break;
				default:
					for (i = 0; i <= safe_w; ++i) {
						total += (long) (pixels[i] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i];
						pixels[i] = (short) (total / kernel_width);
					}

					break;
			}

			for (; i < w; ++i) {
				total -= buffer[i & (8 - 1)];
				pixels[i] = (short) (total / kernel_width);
			}

			pixels += stride_in_shorts;
		}
	}

	public static void stbtt__v_prefilter(FakePtrByte pixels, int w, int h, int stride_in_shorts,
										  long kernel_width) {
		var buffer = new short[8];
		int safe_h = (int) (h - kernel_width);
		int j = 0;
		Array.Clear(buffer, 0, 8);
		for (j = 0; j < w; ++j) {
			int i = 0;
			long total = 0;
			Array.Clear(buffer, 0, (int) kernel_width);
			total = 0;
			switch (kernel_width) {
				case 2:
					for (i = 0; i <= safe_h; ++i) {
						total += (long) (pixels[i * stride_in_shorts] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i * stride_in_shorts];
						pixels[i * stride_in_shorts] = (short) (total / 2);
					}

					break;
				case 3:
					for (i = 0; i <= safe_h; ++i) {
						total += (long) (pixels[i * stride_in_shorts] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i * stride_in_shorts];
						pixels[i * stride_in_shorts] = (short) (total / 3);
					}

					break;
				case 4:
					for (i = 0; i <= safe_h; ++i) {
						total += (long) (pixels[i * stride_in_shorts] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i * stride_in_shorts];
						pixels[i * stride_in_shorts] = (short) (total / 4);
					}

					break;
				case 5:
					for (i = 0; i <= safe_h; ++i) {
						total += (long) (pixels[i * stride_in_shorts] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i * stride_in_shorts];
						pixels[i * stride_in_shorts] = (short) (total / 5);
					}

					break;
				default:
					for (i = 0; i <= safe_h; ++i) {
						total += (long) (pixels[i * stride_in_shorts] - buffer[i & (8 - 1)]);
						buffer[(i + kernel_width) & (8 - 1)] = pixels[i * stride_in_shorts];
						pixels[i * stride_in_shorts] = (short) (total / kernel_width);
					}

					break;
			}

			for (; i < h; ++i) {
				total -= buffer[i & (8 - 1)];
				pixels[i * stride_in_shorts] = (short) (total / kernel_width);
			}

			pixels += 1;
		}
	}

	public static float stbtt__oversample_shift(int oversample) {
		if (oversample == 0)
			return 0.0f;
		return -(oversample - 1) / (2.0f * oversample);
	}

	public static void stbtt_GetScaledFontVMetrics(short[] fontdata, int index, float size, ref float ascent,
												   ref float descent, ref float lineGap) {
		int i_ascent = 0;
		int i_descent = 0;
		int i_lineGap = 0;
		float scale = 0;
		FontInfo info = new FontInfo();
		info.stbtt_InitFont(fontdata, stbtt_GetFontOffsetForIndex(fontdata, index));
		scale = size > 0 ? info.stbtt_ScaleForPixelHeight(size) : info.stbtt_ScaleForMappingEmToPixels(-size);
		info.stbtt_GetFontVMetrics(out i_ascent, out i_descent, out i_lineGap);
		ascent = i_ascent * scale;
		descent = i_descent * scale;
		lineGap = i_lineGap * scale;
	}

	public static void stbtt_GetPackedQuad(stbtt_packedchar[] chardata, int pw, int ph, int char_index,
										   ref float xpos, ref float ypos, ref stbtt_aligned_quad q, int align_to_integer) {
		var ipw = 1.0f / pw;
		var iph = 1.0f / ph;
		var b = chardata[char_index];
		if (align_to_integer != 0) {
			float x = (float) (int) Math.Floor(xpos + b.xoff + 0.5f);
			float y = (float) (int) Math.Floor(ypos + b.yoff + 0.5f);
			q.x0 = x;
			q.y0 = y;
			q.x1 = x + b.xoff2 - b.xoff;
			q.y1 = y + b.yoff2 - b.yoff;
		} else {
			q.x0 = xpos + b.xoff;
			q.y0 = ypos + b.yoff;
			q.x1 = xpos + b.xoff2;
			q.y1 = ypos + b.yoff2;
		}

		q.s0 = b.x0 * ipw;
		q.t0 = b.y0 * iph;
		q.s1 = b.x1 * ipw;
		q.t1 = b.y1 * iph;
		xpos += b.xadvance;
	}

	public static int stbtt__ray_intersect_bezier(float[] orig, float[] ray, float[] q0, float[] q1, float[] q2,
												  float[] hits) {
		var q0perp = q0[1] * ray[0] - q0[0] * ray[1];
		var q1perp = q1[1] * ray[0] - q1[0] * ray[1];
		var q2perp = q2[1] * ray[0] - q2[0] * ray[1];
		var roperp = orig[1] * ray[0] - orig[0] * ray[1];
		var a = q0perp - 2 * q1perp + q2perp;
		var b = q1perp - q0perp;
		var c = q0perp - roperp;
		float s0 = (float) 0;
		float s1 = (float) 0;
		int num_s = 0;
		if (a != 0.0) {
			var discr = b * b - a * c;
			if (discr > 0.0) {
				var rcpna = -1 / a;
				float d = (float) Math.sqrt(discr);
				s0 = (b + d) * rcpna;
				s1 = (b - d) * rcpna;
				if (s0 >= 0.0 && s0 <= 1.0)
					num_s = 1;
				if (d > 0.0 && s1 >= 0.0 && s1 <= 1.0) {
					if (num_s == 0)
						s0 = s1;
					++num_s;
				}
			}
		} else {
			s0 = c / (-2 * b);
			if (s0 >= 0.0 && s0 <= 1.0)
				num_s = 1;
		}

		if (num_s == 0)
			return 0;

		var rcp_len2 = 1 / (ray[0] * ray[0] + ray[1] * ray[1]);
		var rayn_x = ray[0] * rcp_len2;
		var rayn_y = ray[1] * rcp_len2;
		var q0d = q0[0] * rayn_x + q0[1] * rayn_y;
		var q1d = q1[0] * rayn_x + q1[1] * rayn_y;
		var q2d = q2[0] * rayn_x + q2[1] * rayn_y;
		var rod = orig[0] * rayn_x + orig[1] * rayn_y;
		var q10d = q1d - q0d;
		var q20d = q2d - q0d;
		var q0rd = q0d - rod;
		hits[0] = q0rd + s0 * (2.0f - 2.0f * s0) * q10d + s0 * s0 * q20d;
		hits[1] = a * s0 + b;
		if (num_s > 1) {
			hits[2] = q0rd + s1 * (2.0f - 2.0f * s1) * q10d + s1 * s1 * q20d;
			hits[3] = a * s1 + b;
			return 2;
		}

		return 1;
	}

	public static int equal(float[] a, float[] b) {
		return a[0] == b[0] && a[1] == b[1] ? 1 : 0;
	}

	public static int stbtt__compute_crossings_x(float x, float y, int nverts, stbtt_vertex[] verts) {
		int i = 0;
		var orig = new float[2];
		var ray = new float[2];
		ray[0] = 1;
		ray[1] = 0;

		float y_frac = 0;
		int winding = 0;
		orig[0] = x;
		orig[1] = y;
		y_frac = y % 1.0f;
		if (y_frac < 0.01f)
			y += 0.01f;
		else if (y_frac > 0.99f)
			y -= 0.01f;
		orig[1] = y;
		for (i = 0; i < nverts; ++i) {
			if (verts[i].type == STBTT_vline) {
				int x0 = (int) verts[i - 1].x;
				int y0 = (int) verts[i - 1].y;
				int x1 = (int) verts[i].x;
				int y1 = (int) verts[i].y;
				if (y > (y0 < y1 ? y0 : y1) && y < (y0 < y1 ? y1 : y0) && x > (x0 < x1 ? x0 : x1)) {
					var x_inter = (y - y0) / (y1 - y0) * (x1 - x0) + x0;
					if (x_inter < x)
						winding += y0 < y1 ? 1 : -1;
				}
			}

			if (verts[i].type == STBTT_vcurve) {
				int x0 = (int) verts[i - 1].x;
				int y0 = (int) verts[i - 1].y;
				int x1 = (int) verts[i].cx;
				int y1 = (int) verts[i].cy;
				int x2 = (int) verts[i].x;
				int y2 = (int) verts[i].y;
				var ax = x0 < (x1 < x2 ? x1 : x2) ? x0 : x1 < x2 ? x1 : x2;
				var ay = y0 < (y1 < y2 ? y1 : y2) ? y0 : y1 < y2 ? y1 : y2;
				var by = y0 < (y1 < y2 ? y2 : y1) ? y1 < y2 ? y2 : y1 : y0;
				if (y > ay && y < by && x > ax) {
					var q0 = new float[2];
					var q1 = new float[2];
					var q2 = new float[2];
					var hits = new float[4];
					q0[0] = x0;
					q0[1] = y0;
					q1[0] = x1;
					q1[1] = y1;
					q2[0] = x2;
					q2[1] = y2;
					if (equal(q0, q1) != 0 || equal(q1, q2) != 0) {
						x0 = verts[i - 1].x;
						y0 = verts[i - 1].y;
						x1 = verts[i].x;
						y1 = verts[i].y;
						if (y > (y0 < y1 ? y0 : y1) && y < (y0 < y1 ? y1 : y0) && x > (x0 < x1 ? x0 : x1)) {
							var x_inter = (y - y0) / (y1 - y0) * (x1 - x0) + x0;
							if (x_inter < x)
								winding += y0 < y1 ? 1 : -1;
						}
					} else {
						var num_hits = stbtt__ray_intersect_bezier(orig, ray, q0, q1, q2, hits);
						if (num_hits >= 1)
							if (hits[0] < 0)
								winding += hits[1] < 0 ? -1 : 1;
						if (num_hits >= 2)
							if (hits[2] < 0)
								winding += hits[3] < 0 ? -1 : 1;
					}
				}
			}
		}

		return winding;
	}

	public static float stbtt__cuberoot(float x) {
		if (x < 0)
			return -(float) Math.Pow(-x, 1.0f / 3.0f);
		return (float) Math.Pow(x, 1.0f / 3.0f);
	}

	public static int stbtt__solve_cubic(float a, float b, float c, float[] r) {
		var s = -a / 3;
		var p = b - a * a / 3;
		var q = a * (2 * a * a - 9 * b) / 27 + c;
		var p3 = p * p * p;
		var d = q * q + 4 * p3 / 27;
		if (d >= 0) {
			float z = (float) Math.sqrt(d);
			var u = (-q + z) / 2;
			var v = (-q - z) / 2;
			u = stbtt__cuberoot(u);
			v = stbtt__cuberoot(v);
			r[0] = s + u + v;
			return 1;
		} else {
			float u = (float) Math.sqrt(-p / 3);
			float v = (float) Math.Acos(-Math.sqrt(-27 / p3) * q / 2) / 3;
			float m = (float) Math.Cos(v);
			float n = (float) Math.Cos(v - 3.141592 / 2) * 1.732050808f;
			r[0] = s + u * 2 * m;
			r[1] = s - u * (m + n);
			r[2] = s - u * (m - n);
			return 3;
		}
	}

	public static int stbtt__CompareUTF8toUTF16_bigendian_prefix(FakePtrByte s1, int len1, FakePtrByte s2,
																 int len2) {
		int i = 0;
		while (len2 != 0) {
			var ch = (int) (s2[0] * 256 + s2[1]);
			if (ch < 0x80) {
				if (i >= len1)
					return -1;
				if (s1[i++] != ch)
					return -1;
			} else if (ch < 0x800) {
				if (i + 1 >= len1)
					return -1;
				if (s1[i++] != 0xc0 + (ch >> 6))
					return -1;
				if (s1[i++] != 0x80 + (ch & 0x3f))
					return -1;
			} else if (ch >= 0xd800 && ch < 0xdc00) {
				long c = 0;
				var ch2 = (int) (s2[2] * 256 + s2[3]);
				if (i + 3 >= len1)
					return -1;
				c = (long) (((ch - 0xd800) << 10) + (ch2 - 0xdc00) + 0x10000);
				if (s1[i++] != 0xf0 + (c >> 18))
					return -1;
				if (s1[i++] != 0x80 + ((c >> 12) & 0x3f))
					return -1;
				if (s1[i++] != 0x80 + ((c >> 6) & 0x3f))
					return -1;
				if (s1[i++] != 0x80 + (c & 0x3f))
					return -1;
				s2 += 2;
				len2 -= 2;
			} else if (ch >= 0xdc00 && ch < 0xe000) {
				return -1;
			} else {
				if (i + 2 >= len1)
					return -1;
				if (s1[i++] != 0xe0 + (ch >> 12))
					return -1;
				if (s1[i++] != 0x80 + ((ch >> 6) & 0x3f))
					return -1;
				if (s1[i++] != 0x80 + (ch & 0x3f))
					return -1;
			}

			s2 += 2;
			len2 -= 2;
		}

		return i;
	}

	public static int stbtt_CompareUTF8toUTF16_bigendian_internal(FakePtrByte s1, int len1, FakePtrByte s2,
																  int len2) {
		return len1 == stbtt__CompareUTF8toUTF16_bigendian_prefix(s1, len1, s2, len2) ? 1 : 0;
	}

	public static int stbtt__matchpair(FakePtrByte fc, long nm, FakePtrByte name, int nlen, int target_id,
									   int next_id) {
		int i = 0;
		int count = (int) ttUSHORT(fc + nm + 2);
		int stringOffset = (int) (nm + ttUSHORT(fc + nm + 4));
		for (i = 0; i < count; ++i) {
			var loc = (long) (nm + 6 + 12 * i);
			int id = (int) ttUSHORT(fc + loc + 6);
			if (id == target_id) {
				int platform = (int) ttUSHORT(fc + loc + 0);
				int encoding = (int) ttUSHORT(fc + loc + 2);
				int language = (int) ttUSHORT(fc + loc + 4);
				if (platform == 0 || platform == 3 && encoding == 1 || platform == 3 && encoding == 10) {
					int slen = (int) ttUSHORT(fc + loc + 8);
					int off = (int) ttUSHORT(fc + loc + 10);
					var matchlen =
							stbtt__CompareUTF8toUTF16_bigendian_prefix(name, nlen, fc + stringOffset + off, slen);
					if (matchlen >= 0) {
						if (i + 1 < count && ttUSHORT(fc + loc + 12 + 6) == next_id &&
								ttUSHORT(fc + loc + 12) == platform && ttUSHORT(fc + loc + 12 + 2) == encoding &&
								ttUSHORT(fc + loc + 12 + 4) == language) {
							slen = ttUSHORT(fc + loc + 12 + 8);
							off = ttUSHORT(fc + loc + 12 + 10);
							if (slen == 0) {
								if (matchlen == nlen)
									return 1;
							} else if (matchlen < nlen && name[matchlen] == ' ') {
								++matchlen;
								if (stbtt_CompareUTF8toUTF16_bigendian_internal(name + matchlen, nlen - matchlen,
										fc + stringOffset + off, slen) != 0)
									return 1;
							}
						} else {
							if (matchlen == nlen)
								return 1;
						}
					}
				}
			}
		}

		return 0;
	}

	public static int stbtt__matches(short[] data, long offset, FakePtrByte name, int flags) {
		int nlen = 0;
		var ptr = name;

		while (ptr.GetAndIncrease() != '\0')
			ptr++;

		nlen = ptr.Offset - name.Offset - 1;
		long nm = 0;
		long hd = 0;

		var fc = new FakePtrByte(data);
		if (stbtt__isfont(fc + offset) == 0)
			return 0;
		if (flags != 0) {
			hd = stbtt__find_table(fc, offset, "head");
			if ((ttUSHORT(fc + hd + 44) & 7) != (flags & 7))
				return 0;
		}

		nm = stbtt__find_table(fc, offset, "name");
		if (nm == 0)
			return 0;
		if (flags != 0) {
			if (stbtt__matchpair(fc, nm, name, nlen, 16, -1) != 0)
				return 1;
			if (stbtt__matchpair(fc, nm, name, nlen, 1, -1) != 0)
				return 1;
			if (stbtt__matchpair(fc, nm, name, nlen, 3, -1) != 0)
				return 1;
		} else {
			if (stbtt__matchpair(fc, nm, name, nlen, 16, 17) != 0)
				return 1;
			if (stbtt__matchpair(fc, nm, name, nlen, 1, 2) != 0)
				return 1;
			if (stbtt__matchpair(fc, nm, name, nlen, 3, -1) != 0)
				return 1;
		}

		return 0;
	}

	public static int stbtt_FindMatchingFont_internal(short[] font_collection, FakePtrByte name_utf8, int flags) {
		int i = 0;
		for (i = 0; ; ++i) {
			var off = stbtt_GetFontOffsetForIndex(font_collection, i);
			if (off < 0)
				return off;
			if (stbtt__matches(font_collection, (long) off, name_utf8, flags) != 0)
				return off;
		}
	}

	public static int stbtt_BakeFontBitmap(short[] data, int offset, float pixel_height, FakePtrByte pixels,
										   int pw, int ph, int first_char, int num_chars, stbtt_bakedchar[] chardata) {
		return stbtt_BakeFontBitmap_internal(data, offset, pixel_height, pixels, pw, ph, first_char, num_chars,
				chardata);
	}

	public static int stbtt_GetFontOffsetForIndex(short[] data, int index) {
		return stbtt_GetFontOffsetForIndex_internal(new FakePtrByte(data), index);
	}

	public static int stbtt_GetNumberOfFonts(FakePtrByte data) {
		return stbtt_GetNumberOfFonts_internal(data);
	}

	public static int stbtt_FindMatchingFont(short[] fontdata, FakePtrByte name, int flags) {
		return stbtt_FindMatchingFont_internal(fontdata, name, flags);
	}

	public static int stbtt_CompareUTF8toUTF16_bigendian(FakePtrByte s1, int len1, FakePtrByte s2, int len2) {
		return stbtt_CompareUTF8toUTF16_bigendian_internal(s1, len1, s2, len2);
	}
}
