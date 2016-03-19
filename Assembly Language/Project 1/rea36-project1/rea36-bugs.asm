# Rebecca Addison
# rea36@pitt.edu
	.data
q:	.space	512
endT:	.word	120000
msg1:	.asciiz	"The game score is "
msg2:	.asciiz " : "
msg3:	.asciiz	"."
	.text
preGame:
	la	$v0, 0xffff0000		# address for reading key press status
	lw	$t0, 0($v0)		# read the key press status
	andi	$t0, $t0, 1
	beq	$t0, $zero, preGame	# no key pressed
	lw	$t0, 4($v0)		# read key value
	li	$t1, 0x42		# check for center key press
	bne	$t0, $t1, preGame	# wasn't center key
	li	$v0, 30			# start game
	syscall
	move	$s1, $a0		# time step
	move	$s3, $s1		# initial time (never changes)
	la	$k0, q			# start
	la	$k1, q			# end
	li	$s0, 31			# initialize player at center x
	move	$a0, $s0
	li	$a1, 63
	li	$a2, 2
	jal	_setLED
loop:
	la	$v0, 0xffff0000		# address for reading key press status
	lw	$t0, 0($v0)		# read the key press status
	andi	$t0, $t0, 1
	beq	$t0, $zero, endKeyCheck	# no key pressed
	lw	$t0, 4($v0)		# read key value
leftKey:
	li	$t1, 0xE2		# value for left key press
	bne	$t0, $t1, rightKey	# wasn't left key, so try right key
	move	$a0, $s0
	li	$a1, 63
	li	$a2, 0
	jal	_setLED			# turn off LED
	addi	$s0, $s0, -1		# move one left
	slt	$t0, $s0, $0
	beq	$t0, $zero, noWrap
	li	$s0, 63			# wrap around
noWrap:
	move	$a0, $s0
	li	$a1, 63
	li	$a2, 2
	jal	_setLED			# turn on LED
	j	endKeyCheck
rightKey:
	li	$t1, 0xE3		# check for right key press
	bne	$t0, $t1, upKey		# wasn't right key, so check for up
	move	$a0, $s0
	li	$a1, 63
	li	$a2, 0
	jal	_setLED			# turn off LED
	addi	$s0, $s0, 1		# move one right
	andi	$s0, $s0, 0x3F		# wrap around
	move	$a0, $s0
	li	$a2, 2
	jal	_setLED			# turn on LED
	j	endKeyCheck
upKey:
	li	$t1, 0xE0		# value for up key press
	bne	$t0, $t1, downKey	# wasn't up key, so check for center
	# TO DO: Don't create pulse if pulse already there
	addi	$s5, $s5, 1
	move	$a0, $s0		# create pulse event
	li	$a1, 62
	li	$a2, 1
	jal	_setLED
	li	$a2, 0
	li	$a3, 2
	jal	_insert_q		# insert pulse event into queue
	j	endKeyCheck
downKey:
	li	$t1, 0xE1		# check for down key press
	beq	$t0, $t1, endGame
	li	$t1, 0x42		# check for center key press
	beq	$t0, $t1, endGame
endKeyCheck:
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
	addi	$s7, $s7, 1		# counter
	jal	_generateBugs
	jal	_length_q
	move	$s4, $v0		# # of events
	slti	$t0, $s4, 1		# if # of events < 1, $t0 = 1
	bne	$t0, $zero, exitAnimate
animateLoop:
	jal	_remove_q
	lbu	$a0, 0($v0)		# x
	lbu	$a1, 1($v0)		# y
	lbu	$a2, 2($v0)		# r
	lbu	$a3, 3($v0)		# type
	li	$t0, 1
	beq	$a3, $t0, bugEvent
	li	$t0, 2
	beq	$a3, $t0, pulseEvent
	li	$t0, 3
	beq	$a3, $t0, waveEvent
	j	exitAnimate
bugEvent:
	jal	_processBug
	j	continueAnimate
pulseEvent:
	jal	_processPulse
	j	continueAnimate
waveEvent:
	jal	_processWave
continueAnimate:
	jal	_insert_q
	addi	$s4, $s4, -1
	beq	$s4, $zero, exitAnimate	# exit after all events handled
	j	animateLoop
exitAnimate:
	move	$a0, $s0
	li	$a1, 63
	li	$a2, 2
	jal	_setLED			# turn on shooter LED in case it got hit
	li	$v0, 30
	syscall
	move	$s1, $a0		# new time step
	j	loop
endGame:
	la	$a0, msg1
	li	$v0, 4
	syscall
	move	$a0, $s6
	li	$v0, 1
	syscall
	la	$a0, msg2
	li	$v0, 4
	syscall
	move	$a0, $s5
	li	$v0, 1
	syscall
	la	$a0, msg3
	li	$v0, 4
	syscall
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
	beq	$a3, $zero, exitInsert
	sb	$a0, 0($k0)
	sb	$a1, 1($k0)
	sb	$a2, 2($k0)
	sb	$a3, 3($k0)
	addi	$k0, $k0, 4
	la	$t0, q
	addi	$t0, $t0, 511
	and	$k0, $k0, $t0
exitInsert:
	jr	$ra
 
	# int _remove_q()
	# removes event from queue at end and moves end
	# returns: $v0 = address of event info
	# trashes: $t0
_remove_q:
	la	$v0, 0($k1)
	addi	$k1, $k1, 4
	la	$t0, q
	addi	$t0, $t0, 511
	and	$k1, $k1, $t0
exitRemove:
	jr	$ra
	
	# _length_q()
	# returns number of events in queue
	# returns: $v0 = number of events
	# trashes: $t0, $t1
_length_q:
	slt	$t0, $k0, $k1		# if $k0 >= $k1, $t0 = 0
	bne	$t0, $zero, midWrap
	sub	$v0, $k0, $k1
	j	lengthExit
midWrap:
	sub	$v0, $k1, $k0
	li	$t1, 511
	sub	$v0, $t1, $v0
lengthExit:
	srl	$v0, $v0, 2		# divide by 4 for # events
	jr	$ra
	
	# _searchForBug(x,y)
	# finds and kills matching bug in queue
	# arguments: $a0 = x, $a1 = y
	# trashes: $t0-$t2
_searchForBug:
	slt	$t0, $k1, $k0
	beq	$t0, $zero, bugSearch2
	move	$t0, $k1
bugSearchLoop:
	beq	$k0, $t0, exitBugSearch
	addi	$t0, $t0, 4
	lbu	$t2, -4($t0)
	bne	$t2, $a0, bugSearchLoop
	lbu	$t2, -3($t0)
	bne	$t2, $a1, bugSearchLoop
	li	$t1, 0				# kill bug
	sb	$t1, -1($t0)
	j	exitBugSearch
bugSearch2:					# end > start, mid wrap
	la	$t0, q
	addi	$t1, $t0, 511
	move	$t0, $k0
bugSearchLoop2:
	and	$t0, $t0, $t1
	beq	$k1, $t0, exitBugSearch
	addi	$t0, $t0, 4
	lbu	$t2, -4($t0)
	bne	$t2, $a0, bugSearchLoop2
	lbu	$t2, -3($t0)
	bne	$t2, $a1, bugSearchLoop2
	li	$t1, 0				# kill bug
	sb	$t1, -1($t0)
exitBugSearch:
	jr	$ra
	
	# _searchForPulse(x,y)
	# finds and turns matching pulse into wave
	# arguments: $a0 = x, $a1 = y
	# trashes: $t0-$t2
_searchForPulse:
	slt	$t0, $k1, $k0
	beq	$t0, $zero, pulseSearch2
	move	$t0, $k1
pulseSearchLoop:
	beq	$k0, $t0, exitPulseSearch
	addi	$t0, $t0, 4
	lbu	$t2, -4($t0)
	bne	$t2, $a0, pulseSearchLoop
	lbu	$t2, -3($t0)
	bne	$t2, $a1, pulseSearchLoop
	li	$t1, 3				# turns into wave
	sb	$t1, -1($t0)
	j	exitPulseSearch
pulseSearch2:					# end > start, mid wrap
	la	$t0, q
	addi	$t1, $t0, 511
	move	$t0, $k0
pulseSearchLoop2:
	and	$t0, $t0, $t1
	beq	$k1, $t0, exitPulseSearch
	addi	$t0, $t0, 4
	lbu	$t2, -4($t0)
	bne	$t2, $a0, pulseSearchLoop2
	lbu	$t2, -3($t0)
	bne	$t2, $a1, pulseSearchLoop2
	li	$t1, 3
	sb	$t1, -1($t0)
exitPulseSearch:
	jr	$ra
	
	# _generateBugs()
	# generates 3 bugs at x,0
	# where x is between 0 and 63
	# trashes: $t0-$t3
_generateBugs:
	addi	$sp, $sp, -4
	sw	$ra, 0($sp)
	slti	$t0, $s7, 600
	beq	$t0, $zero, time2
	li	$t9, 2				# bugs to generate
	li	$t0, 15
	div	$s7, $t0
	j	contBugs
time2:
	slti	$t0, $s7, 1200
	beq	$t0, $zero, time3
	li	$t9, 3				# bugs to generate
	li	$t0, 10
	div	$s7, $t0
	j	contBugs
time3:
	li	$t9, 4				# bugs to generate
	li	$t0, 5
	div	$s7, $t0
contBugs:
	mfhi	$t1
	bne	$t1, $zero, exitGenBugs
genBugsLoop:
	slt	$t0, $zero, $t9
	beq	$t0, $zero, exitGenBugs
	addi	$t9, $t9, -1
	li	$v0, 42
	li	$a0, 0
	li	$a1, 64
	syscall					# get random number [0,63] for x
	li	$a1, 0
	li	$a2, 3
	jal	_setLED
	li	$a2, 0
	li	$a3, 1
	jal	_insert_q
	j	genBugsLoop
exitGenBugs:
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
	li	$t0, 2
	beq	$v0, $t0, exitBug
	addi	$s6, $s6, 1
	li	$a3, 0
	jal	_searchForPulse
	j	exitBug
bugNextSpot:
	li	$a2, 3			# turn on LED
	jal	_setLED
	slti	$t0, $a1, 63
	bne	$t0, $zero, exitBug
	li	$a3, 0			# kills bug in shooter row
	li	$a2, 0			# turn off LED
	jal	_setLED
exitBug:
	li	$a2, 0
	lw	$ra, 0($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
	# _processPulse(x,y,0,2)
	# processes phaser event
	# $a0 = x
	# $a1 = y, if no bug, $a1 = $a1 - 1
	# $a2 = 0
	# $a3 = 2, if bug, turns into wave event $a3 = 3
	# trashes: $t0
_processPulse:
	addi	$sp, $sp, -4
	sw	$ra, 0($sp)
	li	$a2, 0			# turn off LED
	jal	_setLED
	addi	$a1, $a1, -1
	slt	$t0, $zero, $a1
	bne	$t0, $zero, notAtTop
	li	$a3, 0
	j	pulseExit
notAtTop:
	jal	_getLED
	beq	$v0, $zero, pulseNextSpot
	addi	$s6, $s6, 1
	jal	_searchForBug
	li	$a3, 3
	li	$a2, 1			# turn on LED for wave
	jal	_setLED
	j	pulseExit
pulseNextSpot:
	li	$a2, 1			# turn on LED
	jal	_setLED
	slti	$t0, $a1, 64
	bne	$t0, $zero, exitBug
	li	$a3, 0			# kills bug if past screen
	li	$a2, 0			# turn off LED
	jal	_setLED
pulseExit:
	li	$a2, 0
	lw	$ra, 0($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
	# _processWave(x,y,r,3)
	# process wave event
	# $a0 = x (center)
	# $a1 = y (center)
	# $a2 = radius, if no bug, $a2 = $a2 + 1
	# $a3 = 3, if radius = 10, kill wave, $a3 = 0
	# trashes: $t0-$t8
_processWave:
	addi	$sp, $sp, -4
	sw	$ra, 0($sp)
	move	$t4, $a0				# base x value of wave
	move	$t5, $a1				# base y value of wave
	move	$t6, $a2				# radius of wave
	add	$a1, $t5, $t6
	li	$a2, 0
	jal	_setLED					# turn off x,y+r
	sub	$a1, $t5, $t6
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip1			# skip if off screen
	jal	_setLED					# turn off x,y-r
skip1:
	add	$a0, $t4, $t6
	move	$a1, $t5
	jal	_setLED					# turn off x+r,y
	sub	$a0, $t4, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip2			# skip if off screen
	jal	_setLED					# turn off x-r,y
skip2:
	add	$a0, $t4, $t6
	add	$a1, $t5, $t6
	jal	_setLED					# turn off x+r,y+r
	sub	$a1, $t5, $t6
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip3			# skip if off screen
	jal	_setLED					# turn off x+r,y-r
skip3:
	sub	$a0, $t4, $t6
	add	$a1, $t5, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip4			# skip if off screen
	jal	_setLED					# turn off x-r,y+r
skip4:
	sub	$a1, $t5, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip5
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip5			# skip if off screen
	jal	_setLED					# turn off x-r,y-r
skip5:
	li	$t0, 10
	bne	$t6, $t0, continueWave
	li	$a3, 0					# if radius = 10, kill wave
	j	exitWave
continueWave:
	addi	$t6, $t6, 1				# check for bug at next position
	li	$t8, 3
	move	$a0, $t4
	add	$a1, $t5, $t6
	jal	_getLED					# check x,y+r
	bne	$v0, $t8, noBug1
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
noBug1:
	sub	$a1, $t5, $t6
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip6			# skip if off screen
	jal	_getLED					# check x,y-r
	bne	$v0, $t8, skip6
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip6:
	add	$a0, $t4, $t6
	move	$a1, $t5
	jal	_getLED					# check x+r,y
	bne	$v0, $t8, noBug2
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
noBug2:
	sub	$a0, $t4, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip7			# skip if off screen
	jal	_getLED					# check x-r,y
	bne	$v0, $t8, skip7
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip7:
	add	$a0, $t4, $t6
	add	$a1, $t5, $t6
	jal	_getLED					# check x+r,y+r
	bne	$v0, $t8, noBug3
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
noBug3:
	sub	$a1, $t5, $t6
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip8			# skip if off screen
	jal	_getLED					# check x+r,y-r
	bne	$v0, $t8, skip8
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip8:
	sub	$a0, $t4, $t6
	add	$a1, $t5, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip9			# skip if off screen
	jal	_getLED					# check x-r,y+r
	bne	$v0, $t8, skip9
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip9:
	sub	$a1, $t5, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip10
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip10			# skip if off screen
	jal	_getLED					# check x-r,y-r
	bne	$v0, $t8, skip10
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip10:
	addi	$t7, $t6, 1				# check 2 past current spot
	move	$a0, $t4
	add	$a1, $t5, $t7
	jal	_getLED					# check x,y+r
	bne	$v0, $t8, noBug4
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
noBug4:
	sub	$a1, $t5, $t7
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip11			# skip if off screen
	jal	_getLED					# check x,y-r
	bne	$v0, $t8, skip11
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip11:
	add	$a0, $t4, $t7
	move	$a1, $t5
	jal	_getLED					# check x+r,y
	bne	$v0, $t8, noBug5
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
noBug5:
	sub	$a0, $t4, $t7
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip12			# skip if off screen
	jal	_getLED					# check x-r,y
	bne	$v0, $t8, skip12
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip12:
	add	$a0, $t4, $t7
	add	$a1, $t5, $t7
	jal	_getLED					# check x+r,y+r
	bne	$v0, $t8, noBug6
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
noBug6:
	sub	$a1, $t5, $t7
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip13			# skip if off screen
	jal	_getLED					# check x+r,y-r
	bne	$v0, $t8, skip13
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip13:
	sub	$a0, $t4, $t7
	add	$a1, $t5, $t7
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip14			# skip if off screen
	jal	_getLED					# check x-r,y+r
	bne	$v0, $t8, skip14
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
skip14:
	sub	$a1, $t5, $t7
	slt	$t0, $a0, $zero
	bne	$t0, $zero, waveNextSpot
	slt	$t0, $a1, $zero
	bne	$t0, $zero, waveNextSpot		# skip if off screen
	jal	_getLED					# check x-r,y-r
	bne	$v0, $t8, waveNextSpot
	jal	_searchForBug
	addi	$s6, $s6, 1
	li	$a2, 0
	li	$a3, 3
	j	exitWave
waveNextSpot:
	move	$a0, $t4
	li	$a2, 1
	add	$a1, $t5, $t6
	jal	_setLED					# turn on x,y+r
	sub	$a1, $t5, $t6
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip16			# skip if off screen
	jal	_setLED					# turn on x,y-r
skip16:
	add	$a0, $t4, $t6
	move	$a1, $t5
	jal	_setLED					# turn on x+r,y
	sub	$a0, $t4, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip17			# skip if off screen
	jal	_setLED					# turn on x-r,y
skip17:
	add	$a0, $t4, $t6
	add	$a1, $t5, $t6
	jal	_setLED					# turn on x+r,y+r
	sub	$a1, $t5, $t6
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip18			# skip if off screen
	jal	_setLED					# turn on x+r,y-r
skip18:
	sub	$a0, $t4, $t6
	add	$a1, $t5, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip19			# skip if off screen
	jal	_setLED					# turn on x-r,y+r
skip19:
	sub	$a1, $t5, $t6
	slt	$t0, $a0, $zero
	bne	$t0, $zero, skip20
	slt	$t0, $a1, $zero
	bne	$t0, $zero, skip20			# skip if off screen
	jal	_setLED					# turn on x-r,y-r
skip20:
	move	$a0, $t4
	move	$a1, $t5
	move	$a2, $t6
exitWave:
	lw	$ra, 0($sp)
	addi	$sp, $sp, 4
	jr	$ra

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
