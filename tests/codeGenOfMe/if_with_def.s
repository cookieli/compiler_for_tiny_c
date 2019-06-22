.file "tests/codeGenOfMe/if_with_def.dcf"
.text
.section .rodata
.LC0:
.string "a is %d\n"
.LC1:
.string "a is %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $80, %rsp
movq $4,-56(%rbp)
movq $1,-48(%rbp)
movq $2,-40(%rbp)
movq $3,-32(%rbp)
movq $5,-24(%rbp)
movq -48(%rbp),%rax
movq -40(%rbp),%r10
imulq %r10,%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rax
movq %rax,-32(%rbp)
movq $9,-72(%rbp)
movq $10,-64(%rbp)
movq -40(%rbp),%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
cmpq $10,%rax
jge .L1
subq $16, %rsp
movq $10,-88(%rbp)
movq $11,-80(%rbp)
movq -88(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $16, %rsp
jmp .L1
.L1:
movq -72(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

