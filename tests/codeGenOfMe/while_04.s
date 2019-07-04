.file "tests/codeGenOfMe/while_04.dcf"
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
movq $1,-16(%rbp)
movq $3,-8(%rbp)
.L3:
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jge .L2
subq $16, %rsp
movq $3,-32(%rbp)
movq $0,-24(%rbp)
.L4:
movq -32(%rbp),%rax
movq -24(%rbp),%r10
cmpq %r10,%rax
jle .L5
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -32(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-32(%rbp)
jmp .L4
.L5:
movq -16(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
addq $16, %rsp
jmp .L3
.L2:
leave
ret

