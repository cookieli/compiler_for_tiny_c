.file "tests/codeGenOfMe/break_01.dcf"
.text
.section .rodata
.LC0:
.string "%d\n"
.LC1:
.string "hello world\n"
.LC2:
.string "in loop %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $0,-8(%rbp)
.L1:
movq -8(%rbp),%rax
cmpq $8,%rax
jge .L2
movq -8(%rbp),%rax
cmpq $5,%rax
jne .L3
movq -8(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
nop
jmp .L2
jmp .L3
.L3:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
jmp .L1
.L2:
nop
.L4:
movq -8(%rbp),%rax
cmpq $0,%rax
jle .L5
movq -8(%rbp),%rax
cmpq $2,%rax
jne .L6
nop
jmp .L5
jmp .L6
.L6:
movq -8(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-8(%rbp)
jmp .L4
.L5:
leave
ret

