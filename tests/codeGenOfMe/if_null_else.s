.file "tests/codeGenOfMe/if_null_else.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "not ok\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $9,-8(%rbp)
movq -8(%rbp),%rax
cmpq $100,%rax
jle .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L1
.L1:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

