#include "library.h"
int main() {
	int i;
	int x = 150;
	int y = 150;
	init_graphics();
	do {
		const char *text = "Enter 'q' to exit loop";
		draw_text(50,50,text,0x07E0);
		sleep_ms(500);
		draw_line(x,y,x+10,y+10,0xF81F);
		sleep_ms(500);
		draw_line(x+10,y+10,x+10,y+20,0xFFE0);
		sleep_ms(500);
		draw_line(x+10,y+20,x,y+30,0x79E0);
		sleep_ms(500);
		draw_line(x,y+30,x-10,y+20,0x001F);
		sleep_ms(500);
		draw_line(x-10,y+20,x-10,y+10,0xFBE0);
		sleep_ms(500);
		draw_line(x-10,y+10,x,y,0xBDF7);
		sleep_ms(700);
		clear_screen();
	} while (getkey() != 'q');
	const char *text2 = "*insert witty remark here*";
	draw_text(250,83,text2,0xF81F);
	for (i = 250; i < 458; i++) {
		draw_pixel(i, 100, 0xF800);
		draw_pixel(i, 101, 0xF800);
		draw_pixel(i, 102, 0xF800);
		draw_pixel(i, 103, 0xFBE0);
		draw_pixel(i, 104, 0xFBE0);
		draw_pixel(i, 105, 0xFBE0);
		draw_pixel(i, 106, 0xFFE0);
		draw_pixel(i, 107, 0xFFE0);
		draw_pixel(i, 108, 0xFFE0);
		draw_pixel(i, 109, 0x07E0);
		draw_pixel(i, 110, 0x07E0);
		draw_pixel(i, 111, 0x07E0);
		draw_pixel(i, 112, 0x07FF);
		draw_pixel(i, 113, 0x07FF);
		draw_pixel(i, 114, 0x07FF);
		draw_pixel(i, 115, 0x001F);
		draw_pixel(i, 116, 0x001F);
		draw_pixel(i, 117, 0x001F);
		draw_pixel(i, 118, 0x780F);
		draw_pixel(i, 119, 0x780F);
		draw_pixel(i, 120, 0x780F);
	}
	sleep_ms(600);
	exit_graphics();
	return 0;
}
