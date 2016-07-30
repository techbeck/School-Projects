#include <stdio.h>
#include <stdlib.h>
#include <string.h>

unsigned char get_rand_card() {
        return (unsigned char) rand() % 52;
}
void convert_card(unsigned char *card) {
        // assumes cards in suit order
        *card = (*card % 12) + 1;
        if (*card > 10) {
                *card = 10;
        }
}
int main() {
        unsigned char dealer;   // holds dealer card total
        unsigned char d_card;
        unsigned char user;     // holds user card total
        unsigned char u_card;
        char dealer_choice = 0;
        char user_choice[20];
        char result = 1;    // 0 = u bust, 1 = u win, 2 = d bust, 3 = d win
        char hit[4];
        strcpy(hit, "hit");
        strcpy(user_choice, hit);  // initially assume hit
        printf("Welcome to Blackjack!\n");
        dealer = get_rand_card();  // initialize to first card
        convert_card(&dealer);
        d_card = get_rand_card();
        convert_card(&d_card);
        user = get_rand_card();  // initialize to first card
        convert_card(&user);
        u_card = get_rand_card();
        convert_card(&u_card);
        printf("\nThe dealer:\n? + %u\n", d_card);
        dealer += d_card;
        printf("\nYou:\n%u + %u = %u\n", user, u_card, (user+u_card));
        user += u_card;
        while ((strcmp(user_choice, hit) == 0) || dealer_choice == 0) {
                printf("\nWould you like to \"hit\" or \"stand\"? ");
                scanf("%19s", user_choice);
                printf("\nThe dealer:\n? + %u ", d_card);
                if (dealer < 17) {
                        d_card = get_rand_card();
                        convert_card(&d_card);
                        dealer += d_card;
                } else {    // dealer stand if 17 or higher
                        dealer_choice = 1;
                        if (dealer > 21) {
                                result = 0;
                                printf("BUSTED!\n");
                                break;
                        }
                        else {
                                printf("\n");
                        }
                }
                if (strcmp(user_choice, hit) == 0) {
                        u_card = get_rand_card();
                        convert_card(&u_card);
                        printf("\nYou:\n%u + %u = %u ", user, u_card, (user+u_card));
                        user += u_card;
                        if (user > 21) {
                                result = 0;
                                printf("BUSTED!\n");
                                break;
                        }
                        else {
                                printf("\n");
                        }
                }
        }
        if (dealer > user) {
                result = 2;
        }
        if (result == 0) {
                printf("\nYou busted. Dealer wins.\n");
        }
        else if (result == 1) {
                printf("\nDealer: %u, You: %u\n", dealer, user);
                printf("You win\n");
        }
        else if (result == 2) {
                printf("\nDealer busted. You win.\n");
        }
        else {
                printf("\nDealer: %u, You: %u\n", dealer, user);
                printf("Dealer wins\n");
        }

        return 0;
}