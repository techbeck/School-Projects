#ifndef _LIBRARY_H_
#define _LIBRARY_H_
void clear_screen();
void exit_graphics();
void init_graphics();
char getkey();
void sleep_ms(long ms);
typedef unsigned short color_t;
void draw_line(int x1, int y1, int x2, int y2, color_t c);
void draw_pixel(int x, int y, color_t c);
void draw_text(int x, int y, const char *text, color_t  c);
#endif
