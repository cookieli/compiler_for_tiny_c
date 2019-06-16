.file "tests/codeGenOfMe/if_06.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "wrong, wrong, wrong\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $160, %rsp
movq $2,-155(%rbp)
movq $3,-147(%rbp)
movq $8,-139(%rbp)
movq $8,-131(%rbp)
movq $7,-123(%rbp)
movq $90,-115(%rbp)
movq $100,-107(%rbp)
movq $400,-99(%rbp)
movb $1,-91(%rbp)
movb $0,-90(%rbp)
movb $0,-89(%rbp)
movq -147(%rbp),%rax
movq $3,%r10
imulq %r10,%rax
movq %rax,-72(%rbp)
movq -155(%rbp),%rax
movq -72(%rbp),%r10
addq %r10,%rax
movq %rax,-80(%rbp)
movq -131(%rbp),%rax
movq $2,%r10
cqto
idivq %r10
movq %rax,-64(%rbp)
movq -80(%rbp),%rax
movq -64(%rbp),%r10
subq %r10,%rax
movq %rax,-88(%rbp)
movq -115(%rbp),%rax
movq $30,%r10
cqto
idivq %r10
movq %rax,-48(%rbp)
movq -99(%rbp),%rax
movq $400,%r10
cqto
idivq %r10
movq %rax,-32(%rbp)
movq -155(%rbp),%rax
movq -139(%rbp),%r10
subq %r10,%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rax
movq $9,%r10
addq %r10,%rax
movq %rax,-24(%rbp)
movq -32(%rbp),%rax
movq -24(%rbp),%r10
imulq %r10,%rax
movq %rax,-40(%rbp)
movq -48(%rbp),%rax
movq -40(%rbp),%r10
addq %r10,%rax
movq %rax,-56(%rbp)
movq -139(%rbp),%rax
movq -131(%rbp),%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -88(%rbp),%rax
movq -56(%rbp),%r10
cmpq %r10,%rax
jle .L1
movb -89(%rbp),%al
cmpb $0,%al
jle .L2
movq -8(%rbp),%rax
movq -107(%rbp),%r10
cmpq %r10,%rax
jle .L1
movb -91(%rbp),%al
cmpb $0,%al
jle .L1
.L2:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
jmp .L2
.L1:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
leave
ret

