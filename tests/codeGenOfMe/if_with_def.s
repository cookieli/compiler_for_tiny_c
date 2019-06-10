.file "tests/codeGenOfMe/if_with_def.dcf"
.text
.section .rodata
.LC0:
.string "a, b ,c is %d, %d, %d\n"
.LC1:
.string "a, b is %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $4,-16(%rbp)
movq $3,-8(%rbp)
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jge .L1
subq $32, %rsp
movq $10,-40(%rbp)
movq $11,-32(%rbp)
movq $20,-24(%rbp)
movq -24(%rbp),%rcx
movq -32(%rbp),%rdx
movq -40(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $32, %rsp
.L1:
movq -8(%rbp),%rdx
movq -16(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

