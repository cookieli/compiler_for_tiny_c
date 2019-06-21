.file "tests/codeGenOfMe/while_01.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $9,-8(%rbp)
.L1:
movq -8(%rbp),%rax
cmpq $6,%rax
jle .L2
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-8(%rbp)
jmp .L1
.L2:
leave
ret

