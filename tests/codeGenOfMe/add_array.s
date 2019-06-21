.file "tests/codeGenOfMe/add_array.dcf"
.text
.section .rodata
.LC0:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $128, %rsp
movq $5,-128(%rbp)
movq $1,-120(%rbp)
movq $2,-112(%rbp)
movq $3,-104(%rbp)
movq $1,-80(%rbp)
movq $2,-72(%rbp)
movq -80(%rbp),%rax
movq -72(%rbp),%r10
imulq %r10,%rax
movq %rax,-56(%rbp)
movq -120(%rbp),%rax
movq -112(%rbp),%r10
addq %r10,%rax
movq %rax,-40(%rbp)
movq -104(%rbp),%rax
movq %rax,-32(%rbp)
movq -40(%rbp),%rax
movq -32(%rbp),%r10
cqto
idivq %r10
movq %rax,-48(%rbp)
movq -56(%rbp),%rax
movq -48(%rbp),%r10
addq %r10,%rax
movq %rax,-64(%rbp)
movq -104(%rbp),%rax
movq -120(%rbp),%r10
imulq %r10,%rax
movq %rax,-16(%rbp)
movq -104(%rbp),%rax
movq -112(%rbp),%r10
imulq %r10,%rax
movq %rax,-8(%rbp)
movq -16(%rbp),%rax
movq -8(%rbp),%r10
addq %r10,%rax
movq %rax,-24(%rbp)
movq -64(%rbp),%rax
movq -24(%rbp),%r10
movq %r10,-120(%rbp,%rax,8)
movq -96(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

