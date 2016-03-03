	.data
endT:	.word	120000
q:	.space	1024
	.text
	li	$v0, 30
	syscall
	move	$s1, $a0		# time step
	move	$s3, $s1
	la	$k0, q+4
	la	$k1, q
	li	$s0, 31			# initialize player at center x
	move	$a0, $s0
	li	$a1, 63
	li	$a2, 2
	jal	_setLED
loop:
	# TO DO keypress stuff
	li	$v0, 30
	syscall
	move	$s2, $a0
	sub	$t1, $s2, $s3
	lw	$t2, endT
	slt	$t1, $t1, $t2
	beq	$t1, $zero, endGame	# ends game after time
	
	sub	$t0, $s2, $s1
	slti	$t0, $t0, 100
	bne	$t0, $zero, loop	# only animate once every 100ms
	# TO DO different number generated depends on time
	li	$v0, 42
	li	$a0, 0
	li	$a1, 100
	syscall
	slt	$t0, $a0, $v0
	beq	$t0, $zero, noNewBug
	slti	$t0, $a0, 38
	beq	$t0, $zero, noNewBug
	jal	_generateBug
noNewBug:
	jal	_length_q
	move	$s4, $v0		# length
animateLoop:
	jal	_remove_q
	lbu	$a0, 0($v0)		# x
	lbu	$a1, 1($v0)		# y
	lbu	$a2, 2($v0)		# r
	lbu	$a3, 3($v0)		# type
	li	$t0, 1
	beq	$a3, $t0, bugEvent
	li	$t0, 2
	beq	$a3, $t0, phaserEvent
	li	$t0, 3
	beq	$a3, $t0, waveEvent
	j	exitAnimate
bugEvent:
	jal	_processBug
	j	continueAnimate
phaserEvent:
	jal	_processPhaser
	j	continueAnimate
waveEvent:
	jal	_processWave
continueAnimate:
	jal	_insert_q
	addi	$s4, $s4, -1
	beq	$s4, $zero, exitAnimate	# exit after all events handled
	j	animateLoop
exitAnimate:
	li	$v0, 30
	syscall
	move	$s1, $a0		# new time step
	j	loop
endGame:
	li	$v0, 10
	syscall

	# _insert_q(x,y,r,type)
	# inserts event into queue at start and moves start
	# arguments:
	# $a0 = x
	# $a1 = y
	# $a2 = r
	# $a3 = type (k = 0, b = 1, p = 2, w = 3)
	# trashes: $t0
_insert_q:
	li	$t0, 0
	beq	$a3, $t0, exitInsert
	sb	$a0, 0($k0)
	sb	$a1, 1($k0)
	sb	$a2, 2($k0)
	sb	$a3, 3($k0)
	addi	$k0, $k0, 4
	la	$t0, q+1024
	slt	$t0, $k0, $t0
	bne	$t0, $zero, exitInsert
	la	$k0, q
exitInsert:
	jr	$ra

	# int _remove_q()
	# removes event from queue at end and moves end
	# returns: $v0 = address of event info
	# trashes: $t0
_remove_q:
	la	$v0, 0($k1)
	addi	$k1, $k1, 4
	la	$t0, q+1024
	slt	$t0, $k1, $t0
	bne	$t0, $zero, exitRemove
	la	$k1, q
exitRemove:
	jr	$ra
	
	# _length_q()
	# returns number of events in queue
	# returns: $v0 = number of events
	# trashes: $t0, $t1
_length_q:
	slt	$t0, $k0, $k1
	beq	$t0, $zero, start_end
	sub	$v0, $k0, $k1
start_end:
	sub	$v0, $k1, $k0
	li	$t1, 1024
	sub	$v0, $t1, $v0
lengthExit:
	srl	$v0, $v0, 2
	jr $ra

	# _generateBug()
	# generates bug event and inserts into queue
	# arguments:
	# $a0 = x, set to random [0,63]
	# $a1 = y, set to 0
	# $a2 = radius, set to 0
	# $a3 = type, set to 1
	# trashes: none
_generateBug:
	addi	$sp, $sp, -4
	sw	$ra, 0($sp)
	li	$v0, 42
	li	$a0, 0
	li	$a1, 64
	syscall				# get random number [0,63] for x
	li	$a1, 0
	li	$a2, 3
	jal	_setLED
	li	$a2, 0
	li	$a3, 1
	jal	_insert_q
	lw	$ra, 0($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
	# _processBug(x,y,0,b)
	# processes bug event
	# arguments:
	# $a0 = x
	# $a1 = y, if not killed, $a1 set to $a1 + 1
	# $a2 = 0, no radius for bugs
	# $a3 = 1, if killed, $a3 set to 0
	# trashes: $t0
_processBug:
	addi	$sp, $sp, -4
	sw	$ra, 0($sp)
	li	$a2, 0			# turn off LED
	jal	_setLED
	addi	$a1, $a1, 1
	jal	_getLED
	beq	$v0, $zero, bugNextSpot
	li	$a3, 3
bugNextSpot:
	li	$a2, 3			# turn on LED
	jal	_setLED
	slti	$t0, $a1, 64
	bne	$t0, $zero, exitBug
	li	$a3, 0			# kills bug if past screen
exitBug:
	li	$a2, 0
	lw	$ra, 0($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
	# _processPhaser(x,y,0,2)
	#
_processPhaser:
	# TO DO
	
	# _processWave(x,y,r,3)
	#
_processWave:
	# TO DO

	# void _setLED(int x, int y, int color)
	#   sets the LED at (x,y) to color
	#   color: 0=off, 1=red, 2=yellow, 3=green
	#
	# arguments: $a0 is x, $a1 is y, $a2 is color
	# trashes:   $t0-$t3
	# returns:   none
	#
_setLED:
	# byte offset into display = y * 16 bytes + (x / 4)
	sll	$t0,$a1,4      # y * 16 bytes
	srl	$t1,$a0,2      # x / 4
	add	$t0,$t0,$t1    # byte offset into display
	li	$t2,0xffff0008 # base address of LED display
	add	$t0,$t2,$t0    # address of byte with the LED
	# now, compute led position in the byte and the mask for it
	andi	$t1,$a0,0x3    # remainder is led position in byte
	neg	$t1,$t1        # negate position for subtraction
	addi	$t1,$t1,3      # bit positions in reverse order
	sll	$t1,$t1,1      # led is 2 bits
	# compute two masks: one to clear field, one to set new color
	li	$t2,3		
	sllv	$t2,$t2,$t1
	not	$t2,$t2        # bit mask for clearing current color
	sllv	$t1,$a2,$t1    # bit mask for setting color
	# get current LED value, set the new field, store it back to LED
	lbu	$t3,0($t0)     # read current LED value	
	and	$t3,$t3,$t2    # clear the field for the color
	or	$t3,$t3,$t1    # set color field
	sb	$t3,0($t0)     # update display
	jr	$ra
	
	# int _getLED(int x, int y)
	#   returns the value of the LED at position (x,y)
	#
	#  arguments: $a0 holds x, $a1 holds y
	#  trashes:   $t0-$t2
	#  returns:   $v0 holds the value of the LED (0, 1, 2 or 3)
	#
_getLED:
	# byte offset into display = y * 16 bytes + (x / 4)
	sll  $t0,$a1,4      # y * 16 bytes
	srl  $t1,$a0,2      # x / 4
	add  $t0,$t0,$t1    # byte offset into display
	la   $t2,0xffff0008
	add  $t0,$t2,$t0    # address of byte with the LED
	# now, compute bit position in the byte and the mask for it
	andi $t1,$a0,0x3    # remainder is bit position in byte
	neg  $t1,$t1        # negate position for subtraction
	addi $t1,$t1,3      # bit positions in reverse order
    	sll  $t1,$t1,1      # led is 2 bits
	# load LED value, get the desired bit in the loaded byte
	lbu  $t2,0($t0)
	srlv $t2,$t2,$t1    # shift LED value to lsb position
	andi $v0,$t2,0x3    # mask off any remaining upper bits
	jr   $ra
