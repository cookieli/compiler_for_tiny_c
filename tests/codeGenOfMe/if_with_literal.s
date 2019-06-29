.file "tests/codeGenOfMe/if_with_literal.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "the wrong world\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $4,-8(%rbp)
movq -8(%rbp),%rax
cmpq $5,%rax
jg .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L1:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L2:
leave
ret

