.file "tests/codeGenOfMe/if_empty.dcf"
.text
.section .rodata
.LC0:
.string "a > 0\n"
.LC1:
.string "hello %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $5,-8(%rbp)
movq -8(%rbp),%rax
cmpq $0,%rax
jge .L1
.L2:
movq -8(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret
.L1:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2

