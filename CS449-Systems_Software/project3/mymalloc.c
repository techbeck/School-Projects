#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"
typedef struct node {
	int status;
	struct node *previous;
	struct node *next;
	int size;
} Node;
static Node *coalesce(Node *, Node *);
static Node *head = NULL;
static Node *tail = NULL;
static const int ALLOCATED = 1;
static const int FREE = 0;
static const int node_size = (int) sizeof(Node);
void *my_firstfit_malloc(int size) {
	Node *this_node = head;
	Node *new_node = NULL;
	// first allocation
	if (head == NULL) {
		head = sbrk(size + node_size);
		head->status = ALLOCATED;
		head->previous = NULL;
		head->next = NULL;
		head->size = size;
		tail = head;
		return (head + 1);
	}
	while (this_node->next != NULL) {
		// skips allocated space
		if (this_node->status == ALLOCATED) {
			this_node = this_node->next;
			continue;
		}
		// allocating w/i freed space
		if (this_node->size >= size) {
			// only create new free node if there's space for it
			if (this_node->size > (size + node_size)) {
				new_node = this_node + 1 + (size/node_size);
				new_node->status = FREE;
				new_node->previous = this_node;
				new_node->next = this_node->next;
				new_node->size = this_node->size - size - node_size;
				this_node->next = new_node;
				this_node->size = size;
			}
			this_node->status = ALLOCATED;
			return (this_node + 1);
		}
		this_node = this_node->next;
	}
	// new allocation
	this_node->next = sbrk(size + node_size);
	new_node = this_node->next;
	new_node->status = ALLOCATED;
	new_node->previous = this_node;
	new_node->next = NULL;
	new_node->size = size;
	tail = new_node;    return (new_node + 1);
}
void my_free(void *ptr) {
	Node *this_node = ptr - node_size;
	if (this_node == tail) {
		tail = this_node->previous;
	}
	if (this_node == head && this_node->next == NULL) {
		head = NULL;
	}
	Node *next_node = this_node->next;
	Node *previous_node = this_node->previous;
	this_node->status = FREE;
	if (next_node != NULL && next_node->status == FREE) {
		this_node = coalesce(this_node, next_node);
	}
	if (previous_node != NULL && previous_node->status == FREE) {
		this_node = coalesce(previous_node, this_node);
	}
	ptr = this_node + 1;
	if ((ptr + this_node->size) == sbrk(0)) {
		sbrk(-(this_node->size + node_size));
	} // decrement brk if freed space borders it
}
static Node *coalesce(Node *fst_node, Node *snd_node) {
	fst_node->next = snd_node->next;
	if (snd_node->next != NULL) {
		snd_node->next->previous = fst_node;
	}
	fst_node->size = fst_node->size + snd_node->size + node_size;
}