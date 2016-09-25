#include <linux/fb.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <sys/select.h>
#include <termios.h>
#include <time.h>
#include "iso_font.h"
#include "library.h"
#define BMASK(c) (c & 0x001F) // Blue mask
#define GMASK(c) (c & 0x07E0) // Green mask
#define RMASK(c) (c & 0xF800) // Red mask
int fid; // frame buffer file descriptor
color_t *address;
size_t buffer_size;
struct fb_var_screeninfo var_info;
struct fb_fix_screeninfo fixed_info;
struct termios old_term, new_term;
size_t buffer_size;
void init_graphics() {
	// Uncomment line below to automatically clear when initializing
	clear_screen();
	fid = open("/dev/fb0", O_RDWR);
	ioctl(fid, FBIOGET_VSCREENINFO, &var_info);
	ioctl(fid, FBIOGET_FSCREENINFO, &fixed_info);
	buffer_size = var_info.yres_virtual * fixed_info.line_length;
	address = mmap(0, buffer_size, PROT_READ | PROT_WRITE, MAP_SHARED, fid, 0);
	ioctl(STDIN_FILENO, TCGETS, &old_term);
	new_term = old_term;
	new_term.c_lflag &= ~( ICANON | ECHO );
	ioctl(STDIN_FILENO, TCSETS, &new_term);
	// turn off blinking cursor
	write(STDOUT_FILENO, "\033[?25l", 6);
}
void exit_graphics() {
	// Uncomment line below to automatically clear when exiting
	//clear_screen();
	// reset terminal to old settings
	ioctl(STDIN_FILENO, TCSETS, &old_term);
	// turn on cursor
	write(STDOUT_FILENO, "\033[?25h", 6);
	munmap(address, buffer_size);
	close(fid);
}
void clear_screen() {
	write(STDOUT_FILENO, "\033[2J", 6);
}
char getkey() {
	char key = '\0';
	struct timeval time = { .tv_sec = 0, .tv_usec = 0 };
	int in = STDIN_FILENO;
	fd_set fds;
	FD_SET(in,&fds);
	if (select(in+1, &fds, NULL, NULL, &time) > 0) {
		read(in, &key, 1);
	}
	return key;
}
void sleep_ms(long ms) {
	const struct timespec time = { .tv_sec = 0, .tv_nsec = ms*1000000 };
	nanosleep(&time, 0);
}
void draw_pixel(int x, int y, color_t color) {
	if (y >= 480) return; // invalid coordinate
	int coordinate = (y * fixed_info.line_length / sizeof(color_t)) + x;
	*(address + coordinate) = color;
}
void draw_line(int x1, int y1, int x2, int y2, color_t c) {
	int w = x2 - x1;
	int h = y2 - y1;
	int longest, shortest, numerator, i;
	int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
	if (w<0) dx1 = -1; else if (w>0) dx1 = 1;
	if (h<0) dy1 = -1; else if (h>0) dy1 = 1;
	if (w<0) dx2 = -1; else if (w>0) dx2 = 1;
	longest = w;
	if (w<0) longest = -w;
	shortest = h;
	if (h<0) shortest = -h;
	if (!(longest>shortest)) {
		longest = h;
		if (h<0) longest = -h;
		shortest = w;
		if (w<0) shortest = -w;
		if (h<0) dy2 = -1; else if (h>0) dy2 = 1;
		dx2 = 0;
	}
	numerator = longest >> 1;
	for (i=0;i<=longest;i++) {
		draw_pixel(x1,y1,c);
		numerator += shortest;
		if (!(numerator<longest)) {
			numerator -= longest;
			x1 += dx1;
			y1 += dy1;
		} else {
			x1 += dx2;
			y1 += dy2;
		}
	}
}
void draw_character(int x, int y, const char character, color_t c) {
	char row, row_val, column, pixel;
	for (row = 0; row < 16; row++) {
		row_val = iso_font[character * ISO_CHAR_HEIGHT + row];
		for (column = 0; column < 8; column++) {
			pixel = row_val>>column & 1;
			if (pixel) {
				draw_pixel(x+column, y+row, c);
			}
		}
	}
}
void draw_text(int x, int y, const char *text, color_t c) {
	while(*text != '\0') {
		draw_character(x,y,*text,c);
		if (x < fixed_info.line_length / sizeof(color_t)) {
			x += 8;
		} else {
			break;
		}
		text++;
	}
}
