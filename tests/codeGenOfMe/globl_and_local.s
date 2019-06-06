.file "tests/codeGenOfMe/globl_and_local.dcf"
.text
.comm	a,8, 8
.section .rodata
.LC0:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $5,-8(%rbp)
movq -8(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

