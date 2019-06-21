.file "tests/codeGenOfMe/while_05.dcf"
.text
.section .rodata
.LC0:
.string "hello world %d\n"
.LC1:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $4,-48(%rbp)
movq $3,-24(%rbp)
movq -24(%rbp),%rax
movq %rax,-8(%rbp)
.L1:
movq -8(%rbp),%rax
cmpq $6,%rax
jge .L2
subq $16, %rsp
movq -24(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -16(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rax
movq %rax,-24(%rbp)
movq -24(%rbp),%rax
movq $4,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
movq %rax,-16(%rbp)
addq $16, %rsp
jmp .L1
.L2:
movq -16(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

