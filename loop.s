.file "tests/codeGenOfMe/loop_01.dcf"
.text
.comm	A,808,32
.comm	y,8, 8
.section .rodata
.LC0:
.string "tests/codeGenOfMe/loop_01.dcf:15:7:out of array bound error for A\n"
.LC1:
.string "after j: %d\n"
.LC2:
.string "after y: %d\n"
.LC3:
.string "i: %d\n"
.text
.globl clobbery
.type clobbery, @function
clobbery:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
movq y(%rip),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,y(%rip)
.L1:
leave
ret
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $100,A(%rip)
movq $0,-8(%rbp)
.L3:
movq -8(%rbp),%rax
cmpq $30,%rax
jge .L2
subq $16, %rsp
movq $0,-24(%rbp)
.L4:
movq y(%rip),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
movq -24(%rbp),%rax
movq -16(%rbp),%r10
cmpq %r10,%rax
jge .L5
subq $16, %rsp
movq $2,%rax
movq -8(%rbp),%r10
imulq %r10,%rax
movq %rax,-32(%rbp)
movq -32(%rbp),%rax
movq -24(%rbp),%r10
addq %r10,%rax
movq %rax,-40(%rbp)
movq -40(%rbp),%rax
cmpq $100,%rax
jl .L6
.L7:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L8
.L6:
movq -40(%rbp),%rax
cmpq $0,%rax
jge .L8
jmp .L7
.L8:
leaq A+8(%rip),%rax
movq -40(%rbp),%r10
movq $1,(%rax,%r10,8)
movq y(%rip),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,y(%rip)
movq -24(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq y(%rip),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -24(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-24(%rbp)
addq $16, %rsp
jmp .L4
.L5:
movq -8(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
addq $16, %rsp
jmp .L3
.L2:
leave
ret

