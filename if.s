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
subq $96, %rsp
movq $4,-72(%rbp)
movq $1,-64(%rbp)
movq $2,-56(%rbp)
movq $3,-48(%rbp)
movq $5,-40(%rbp)
movq -64(%rbp),%rax
movq %rax,-24(%rbp)
movq -56(%rbp),%rax
movq %rax,-16(%rbp)
movq -24(%rbp),%rax
movq -16(%rbp),%r10
imulq %r10,%rax
movq %rax,-32(%rbp)
movq -32(%rbp),%rax
movq %rax,-48(%rbp)
movq $9,-88(%rbp)
movq $10,-80(%rbp)
movq -8(%rbp),%rax
cmpq $10,%rax
jge .L3
subq $16, %rsp
movq $10,-104(%rbp)
movq $11,-96(%rbp)
movq -104(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $16, %rsp
jmp .L3
.L3:
movq -88(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L2:
leave
ret

