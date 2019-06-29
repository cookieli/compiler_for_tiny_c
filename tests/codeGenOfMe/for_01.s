.file "tests/codeGenOfMe/for_01.dcf"
.text
.section .rodata
.LC0:
.string "a %d\n"
.LC1:
.string "\n"
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
cmpq $10,%rax
jge .L2
subq $16, %rsp
movq $10,-16(%rbp)
movq -16(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
addq $16, %rsp
jmp .L1
.L2:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

