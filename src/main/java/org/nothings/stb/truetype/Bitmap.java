package org.nothings.stb.truetype;

import java.util.Arrays;

import static org.nothings.stb.truetype.Common.*;

public class Bitmap {
	private interface IHolder<T> {
		T getValue();

		void setValue(T value);
	}

	private static class SimpleHolder<T> implements IHolder<T> {
		private T value;

		public SimpleHolder(T val) {
			value = val;
		}

		@Override
		public T getValue() {
			return value;
		}

		@Override
		public void setValue(T value) {
			this.value = value;
		}
	}

	private static class ActiveEdgeNext implements IHolder<stbtt__active_edge> {
		private final stbtt__active_edge edge;

		@Override
		public stbtt__active_edge getValue() {
			return edge.next;
		}

		@Override
		public void setValue(stbtt__active_edge value) {
			edge.next = value;
		}

		public ActiveEdgeNext(stbtt__active_edge p) {
			edge = p;
		}
	}

	public int h;
	public FakePtrByte pixels;
	public int stride;
	public int w;

	public static void stbtt__handle_clipped_edge(float[] scanline, int offset, int x, stbtt__active_edge e, float x0,
												  float y0, float x1, float y1) {
		if (y0 == y1)
			return;
		if (y0 > e.ey)
			return;
		if (y1 < e.sy)
			return;
		if (y0 < e.sy) {
			x0 += (x1 - x0) * (e.sy - y0) / (y1 - y0);
			y0 = e.sy;
		}

		if (y1 > e.ey) {
			x1 += (x1 - x0) * (e.ey - y1) / (y1 - y0);
			y1 = e.ey;
		}

		if (x0 <= x && x1 <= x) {
			scanline[x + offset] += e.direction * (y1 - y0);
		} else if (x0 >= x + 1 && x1 >= x + 1) {
		} else {
			scanline[x + offset] += e.direction * (y1 - y0) * (1 - (x0 - x + (x1 - x)) / 2);
		}
	}

	public static void stbtt__fill_active_edges_new(float[] scanline, int scanline_fill, int len,
													stbtt__active_edge e, float y_top) {
		float y_bottom = y_top + 1;
		while (e != null) {
			if (e.fdx == 0) {
				float x0 = e.fx;
				if (x0 < len) {
					if (x0 >= 0) {
						stbtt__handle_clipped_edge(scanline, 0, (int) x0, e, x0, y_top, x0, y_bottom);
						stbtt__handle_clipped_edge(scanline, scanline_fill - 1, (int) x0 + 1, e, x0, y_top, x0, y_bottom);
					} else {
						stbtt__handle_clipped_edge(scanline, scanline_fill - 1, 0, e, x0, y_top, x0, y_bottom);
					}
				}
			} else {
				float x0 = e.fx;
				float dx = e.fdx;
				float xb = x0 + dx;
				float x_top = 0;
				float x_bottom = 0;
				float sy0 = 0;
				float sy1 = 0;
				float dy = e.fdy;
				if (e.sy > y_top) {
					x_top = x0 + dx * (e.sy - y_top);
					sy0 = e.sy;
				} else {
					x_top = x0;
					sy0 = y_top;
				}

				if (e.ey < y_bottom) {
					x_bottom = x0 + dx * (e.ey - y_top);
					sy1 = e.ey;
				} else {
					x_bottom = xb;
					sy1 = y_bottom;
				}

				if (x_top >= 0 && x_bottom >= 0 && x_top < len && x_bottom < len) {
					if ((int) x_top == (int) x_bottom) {
						float height = 0;
						int x = (int) x_top;
						height = sy1 - sy0;
						scanline[x] += e.direction * (1 - (x_top - x + (x_bottom - x)) / 2) * height;
						scanline[x + scanline_fill] += e.direction * height;
					} else {
						int x = 0;
						int x1 = 0;
						int x2 = 0;
						float y_crossing = 0;
						float step = 0;
						float sign = 0;
						float area = 0;
						if (x_top > x_bottom) {
							float t = 0;
							sy0 = y_bottom - (sy0 - y_top);
							sy1 = y_bottom - (sy1 - y_top);
							t = sy0;
							sy0 = sy1;
							sy1 = t;
							t = x_bottom;
							x_bottom = x_top;
							x_top = t;
							dx = -dx;
							dy = -dy;
							t = x0;
							x0 = xb;
							xb = t;
						}

						x1 = (int) x_top;
						x2 = (int) x_bottom;
						y_crossing = (x1 + 1 - x0) * dy + y_top;
						sign = e.direction;
						area = sign * (y_crossing - sy0);
						scanline[x1] += area * (1 - (x_top - x1 + (x1 + 1 - x1)) / 2);
						step = sign * dy;
						for (x = x1 + 1; x < x2; ++x) {
							scanline[x] += area + step / 2;
							area += step;
						}

						y_crossing += dy * (x2 - (x1 + 1));
						scanline[x2] += area + sign * (1 - (x2 - x2 + (x_bottom - x2)) / 2) * (sy1 - y_crossing);
						scanline[x2 + scanline_fill] += sign * (sy1 - sy0);
					}
				} else {
					int x = 0;
					for (x = 0; x < len; ++x) {
						float y0 = y_top;
						float x1 = (float) x;
						float x2 = (float) (x + 1);
						float x3 = xb;
						float y3 = y_bottom;
						float y1 = (x - x0) / dx + y_top;
						float y2 = (x + 1 - x0) / dx + y_top;
						if (x0 < x1 && x3 > x2) {
							stbtt__handle_clipped_edge(scanline, 0, x, e, x0, y0, x1, y1);
							stbtt__handle_clipped_edge(scanline, 0, x, e, x1, y1, x2, y2);
							stbtt__handle_clipped_edge(scanline, 0, x, e, x2, y2, x3, y3);
						} else if (x3 < x1 && x0 > x2) {
							stbtt__handle_clipped_edge(scanline, 0, x, e, x0, y0, x2, y2);
							stbtt__handle_clipped_edge(scanline, 0, x, e, x2, y2, x1, y1);
							stbtt__handle_clipped_edge(scanline, 0, x, e, x1, y1, x3, y3);
						} else if (x0 < x1 && x3 > x1) {
							stbtt__handle_clipped_edge(scanline, 0, x, e, x0, y0, x1, y1);
							stbtt__handle_clipped_edge(scanline, 0, x, e, x1, y1, x3, y3);
						} else if (x3 < x1 && x0 > x1) {
							stbtt__handle_clipped_edge(scanline, 0, x, e, x0, y0, x1, y1);
							stbtt__handle_clipped_edge(scanline, 0, x, e, x1, y1, x3, y3);
						} else if (x0 < x2 && x3 > x2) {
							stbtt__handle_clipped_edge(scanline, 0, x, e, x0, y0, x2, y2);
							stbtt__handle_clipped_edge(scanline, 0, x, e, x2, y2, x3, y3);
						} else if (x3 < x2 && x0 > x2) {
							stbtt__handle_clipped_edge(scanline, 0, x, e, x0, y0, x2, y2);
							stbtt__handle_clipped_edge(scanline, 0, x, e, x2, y2, x3, y3);
						} else {
							stbtt__handle_clipped_edge(scanline, 0, x, e, x0, y0, x3, y3);
						}
					}
				}
			}

			e = e.next;
		}
	}

	public void stbtt__rasterize_sorted_edges(FakePtr<stbtt__edge> eOriginal, int n, int vsubsample, int off_x, int off_y) {
		FakePtr<stbtt__edge> e = eOriginal.clone();
		SimpleHolder<stbtt__active_edge> active = new SimpleHolder<stbtt__active_edge>(null);
		int y = 0;
		int j = 0;
		int i = 0;
		float[] scanline;
		int scanline2;
		if (w > 64)
			scanline = new float[w * 2 + 1];
		else
			scanline = new float[129];
		scanline2 = w;
		y = off_y;
		e.getAt(n).y0 = (float) (off_y + h) + 1;
		while (j < h) {
			float scan_y_top = y + 0.0f;
			float scan_y_bottom = y + 1.0f;
			IHolder<stbtt__active_edge> step = active;

			Arrays.fill(scanline, 0, w, 0);
			Arrays.fill(scanline, scanline2, w + 1, 0);

			while (step.getValue() != null) {
				stbtt__active_edge z = step.getValue();
				if (z.ey <= scan_y_top) {
					// In original code `step` had pointer to pointer type(stbtt__active_edge **)
					// So `step.getValue() = z.next`(originally `*step = z.next`) was actually setting to z.next
					// whatever `step` was pointing to
					// So this whole complicated logic starting with IHolder<T> is required to reproduce that behavior
					step.setValue(z.next);
					z.direction = 0;
				} else {
					step = new ActiveEdgeNext(step.getValue());
				}
			}

			while (e.get().y0 <= scan_y_bottom) {
				if (e.get().y0 != e.get().y1) {
					stbtt__active_edge z = Common.stbtt__new_active(e.get(), off_x, scan_y_top);
					if (z != null) {
						if (j == 0 && off_y != 0)
							if (z.ey < scan_y_top)
								z.ey = scan_y_top;
						z.next = active.getValue();
						active.setValue(z);
					}
				}

				e.increase();
			}

			if (active.getValue() != null)
				stbtt__fill_active_edges_new(scanline, scanline2 + 1, w, active.getValue(), scan_y_top);
			{
				float sum = (float) 0;
				for (i = 0; i < w; ++i) {
					float k = 0;
					int m = 0;
					sum += scanline[scanline2 + i];
					k = scanline[i] + sum;
					k = (float) Math.abs((double) k) * 255 + 0.5f;
					m = (int) k;
					if (m > 255)
						m = 255;
					pixels.setAt(j * stride + i, (short) m);
				}
			}

			step = active;
			while (step.getValue() != null) {
				stbtt__active_edge z = step.getValue();
				z.fx += z.fdx;
				step = new ActiveEdgeNext(step.getValue());
			}

			++y;
			++j;
		}
	}

	public static void stbtt__sort_edges_ins_sort(FakePtr<stbtt__edge> pOriginal, int n) {
		FakePtr<stbtt__edge> p = pOriginal.clone();
		int i = 0;
		int j = 0;
		for (i = 1; i < n; ++i) {
			stbtt__edge t = p.getAt(i);
			stbtt__edge a = t;
			j = i;
			while (j > 0) {
				stbtt__edge b = p.getAt(j - 1);
				int c = a.y0 < b.y0 ? 1 : 0;
				if (c == 0)
					break;
				p.setAt(j, p.getAt(j - 1));
				--j;
			}

			if (i != j)
				p.setAt(j, t);
		}
	}

	public static void stbtt__sort_edges_quicksort(FakePtr<stbtt__edge> pOriginal, int n) {
		FakePtr<stbtt__edge> p = pOriginal.clone();
		while (n > 12) {
			stbtt__edge t = new stbtt__edge();
			int c01 = 0;
			int c12 = 0;
			int c = 0;
			int m = 0;
			int i = 0;
			int j = 0;
			m = n >> 1;
			c01 = p.getAt(0).y0 < p.getAt(m).y0 ? 1 : 0;
			c12 = p.getAt(m).y0 < p.getAt(n - 1).y0 ? 1 : 0;
			if (c01 != c12) {
				int z = 0;
				c = p.getAt(0).y0 < p.getAt(n - 1).y0 ? 1 : 0;
				z = c == c12 ? 0 : n - 1;
				t = p.getAt(z);
				p.setAt(z, p.getAt(m));
				p.setAt(m, t);
			}

			t = p.getAt(0);
			p.setAt(0, p.getAt(m));
			p.setAt(m, t);
			i = 1;
			j = n - 1;
			for (; ; ) {
				for (; ; ++i)
					if (!(p.getAt(i).y0 < p.getAt(0).y0))
						break;
				for (; ; --j)
					if (!(p.getAt(0).y0 < p.getAt(j).y0))
						break;
				if (i >= j)
					break;
				t = p.getAt(i);
				p.setAt(i, p.getAt(j));
				p.setAt(j, t);
				++i;
				--j;
			}

			if (j < n - i) {
				stbtt__sort_edges_quicksort(p, j);
				p = p.cloneAdd(i);
				n = n - i;
			} else {
				stbtt__sort_edges_quicksort(p.cloneAdd(i), n - i);
				n = j;
			}
		}
	}

	public static void stbtt__sort_edges(FakePtr<stbtt__edge> p, int n) {
		stbtt__sort_edges_quicksort(p, n);
		stbtt__sort_edges_ins_sort(p, n);
	}

	public static void stbtt__add_point(stbtt__point[] points, int n, float x, float y) {
		if (points == null)
			return;
		points[n].x = x;
		points[n].y = y;
	}

	public static int stbtt__tesselate_curve(stbtt__point[] points, int num_points, float x0, float y0,
											 float x1, float y1, float x2, float y2, float objspace_flatness_squared, int n) {
		float mx = (x0 + 2 * x1 + x2) / 4;
		float my = (y0 + 2 * y1 + y2) / 4;
		float dx = (x0 + x2) / 2 - mx;
		float dy = (y0 + y2) / 2 - my;
		if (n > 16)
			return num_points;
		if (dx * dx + dy * dy > objspace_flatness_squared) {
			num_points = stbtt__tesselate_curve(points, num_points, x0, y0, (x0 + x1) / 2.0f, (y0 + y1) / 2.0f, mx, my,
					objspace_flatness_squared, n + 1);
			num_points = stbtt__tesselate_curve(points, num_points, mx, my, (x1 + x2) / 2.0f, (y1 + y2) / 2.0f, x2, y2,
					objspace_flatness_squared, n + 1);
		} else {
			stbtt__add_point(points, num_points, x2, y2);
			num_points++;
		}

		return num_points;
	}

	public static int stbtt__tesselate_cubic(stbtt__point[] points, int num_points, float x0, float y0,
											 float x1, float y1, float x2, float y2, float x3, float y3, float objspace_flatness_squared, int n) {
		float dx0 = x1 - x0;
		float dy0 = y1 - y0;
		float dx1 = x2 - x1;
		float dy1 = y2 - y1;
		float dx2 = x3 - x2;
		float dy2 = y3 - y2;
		float dx = x3 - x0;
		float dy = y3 - y0;
		float longlen = (float) (Math.sqrt(dx0 * dx0 + dy0 * dy0) + Math.sqrt(dx1 * dx1 + dy1 * dy1) +
				Math.sqrt(dx2 * dx2 + dy2 * dy2));
		float shortlen = (float) Math.sqrt(dx * dx + dy * dy);
		float flatness_squared = longlen * longlen - shortlen * shortlen;
		if (n > 16)
			return num_points;
		if (flatness_squared > objspace_flatness_squared) {
			float x01 = (x0 + x1) / 2;
			float y01 = (y0 + y1) / 2;
			float x12 = (x1 + x2) / 2;
			float y12 = (y1 + y2) / 2;
			float x23 = (x2 + x3) / 2;
			float y23 = (y2 + y3) / 2;
			float xa = (x01 + x12) / 2;
			float ya = (y01 + y12) / 2;
			float xb = (x12 + x23) / 2;
			float yb = (y12 + y23) / 2;
			float mx = (xa + xb) / 2;
			float my = (ya + yb) / 2;
			num_points = stbtt__tesselate_cubic(points, num_points, x0, y0, x01, y01, xa, ya, mx, my,
					objspace_flatness_squared, n + 1);
			num_points = stbtt__tesselate_cubic(points, num_points, mx, my, xb, yb, x23, y23, x3, y3,
					objspace_flatness_squared, n + 1);
		} else {
			stbtt__add_point(points, num_points, x3, y3);
			num_points++;
		}

		return num_points;
	}

	public static Pair<stbtt__point[], int[]> stbtt_FlattenCurves(stbtt_vertex[] vertices, int num_verts, float objspace_flatness) {
		stbtt__point[] points = null;
		int num_points = 0;
		float objspace_flatness_squared = objspace_flatness * objspace_flatness;
		int i = 0;
		int n = 0;
		int start = 0;
		int pass = 0;
		for (i = 0; i < num_verts; ++i)
			if (vertices[i].type == STBTT_vmove)
				++n;

		if (n == 0)
			return null;
		int[] contour_lengths = new int[n];

		for (pass = 0; pass < 2; ++pass) {
			float x = (float) 0;
			float y = (float) 0;
			if (pass == 1)
				points = new stbtt__point[num_points];
			num_points = 0;
			n = -1;
			for (i = 0; i < num_verts; ++i)
				switch (vertices[i].type) {
					case STBTT_vmove:
						if (n >= 0)
							contour_lengths[n] = num_points - start;
						++n;
						start = num_points;
						x = vertices[i].x;
						y = vertices[i].y;
						stbtt__add_point(points, num_points++, x, y);
						break;
					case STBTT_vline:
						x = vertices[i].x;
						y = vertices[i].y;
						stbtt__add_point(points, num_points++, x, y);
						break;
					case STBTT_vcurve:
						num_points = stbtt__tesselate_curve(points, num_points, x, y, vertices[i].cx, vertices[i].cy,
								vertices[i].x, vertices[i].y, objspace_flatness_squared, 0);
						x = vertices[i].x;
						y = vertices[i].y;
						break;
					case STBTT_vcubic:
						num_points = stbtt__tesselate_cubic(points, num_points, x, y, vertices[i].cx, vertices[i].cy,
								vertices[i].cx1, vertices[i].cy1, vertices[i].x, vertices[i].y,
								objspace_flatness_squared, 0);
						x = vertices[i].x;
						y = vertices[i].y;
						break;
				}

			contour_lengths[n] = num_points - start;
		}

		return new Pair<>(points, contour_lengths);
	}

	public void stbtt__rasterize(stbtt__point[] pts, int[] wcount,
								 float scale_x, float scale_y, float shift_x, float shift_y, int off_x, int off_y, int invert) {
		float y_scale_inv = invert != 0 ? -scale_y : scale_y;
		int n = 0;
		int i = 0;
		int j = 0;
		int k = 0;
		int m = 0;
		int vsubsample = 1;
		n = 0;
		for (i = 0; i < wcount.length; ++i)
			n += wcount[i];
		stbtt__edge[] e = new stbtt__edge[n + 1];
		for (i = 0; i < e.length; ++i)
			e[i] = new stbtt__edge();
		n = 0;
		m = 0;
		for (i = 0; i < wcount.length; ++i) {
			FakePtr<stbtt__point> p = new FakePtr<stbtt__point>(pts, m);
			m += wcount[i];
			j = wcount[i] - 1;
			for (k = 0; k < wcount[i]; j = k++) {
				int a = k;
				int b = j;
				if (p.getAt(j).y == p.getAt(k).y)
					continue;
				e[n].invert = 0;
				if (invert != 0 && p.getAt(j).y > p.getAt(k).y || invert == 0 && p.getAt(j).y < p.getAt(k).y) {
					e[n].invert = 1;
					a = j;
					b = k;
				}

				e[n].x0 = p.getAt(a).x * scale_x + shift_x;
				e[n].y0 = (p.getAt(a).y * y_scale_inv + shift_y) * vsubsample;
				e[n].x1 = p.getAt(b).x * scale_x + shift_x;
				e[n].y1 = (p.getAt(b).y * y_scale_inv + shift_y) * vsubsample;
				++n;
			}
		}

		FakePtr<stbtt__edge> ptr = new FakePtr<stbtt__edge>(e);
		stbtt__sort_edges(ptr, n);
		stbtt__rasterize_sorted_edges(ptr, n, vsubsample, off_x, off_y);
	}

	public void stbtt_Rasterize(float flatness_in_pixels, stbtt_vertex[] vertices,
								int num_verts, float scale_x, float scale_y, float shift_x, float shift_y, int x_off, int y_off, int invert) {
		float scale = scale_x > scale_y ? scale_y : scale_x;
		Pair<stbtt__point[], int[]> windings = stbtt_FlattenCurves(vertices, num_verts, flatness_in_pixels / scale);
		if (windings != null)
			stbtt__rasterize(windings.first, windings.second, scale_x, scale_y, shift_x, shift_y,
					x_off, y_off, invert);
	}
}
