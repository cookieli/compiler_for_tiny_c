.file "tests/codeGenOfMe/for_03.dcf"
.text
.section .rodata
.LC0:
.string "%d "
.LC1:
.string "\n"
.LC2:
.string "%d "
.LC3:
.string "\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $208, %rsp
movq $7,-200(%rbp)
movq $0,-136(%rbp)
.L1:
movq -136(%rbp),%rax
movq -200(%rbp),%r10
cmpq %r10,%rax
jge .L2
subq $16, %rsp
movq -136(%rbp),%rax
movq $2,%r10
addq %r10,%rax
movq %rax,-208(%rbp)
movq -136(%rbp),%rax
movq -208(%rbp),%r10
movq %r10,-192(%rbp,%rax,8)
movq -136(%rbp),%rax
movq -192(%rbp,%rax,8),%r10
movq %r10,-216(%rbp)
movq -216(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -136(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-136(%rbp)
addq $16, %rsp
jmp .L1
.L2:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $3,-192(%rbp)
.L3:
movq -176(%rbp),%rax
movq $2,%r10
subq %r10,%rax
movq %rax,-104(%rbp)
movq -192(%rbp),%rax
movq %rax,-96(%rbp)
movq -104(%rbp),%rax
movq -96(%rbp),%r10
addq %r10,%rax
movq %rax,-112(%rbp)
movq -184(%rbp),%rax
movq %rax,-88(%rbp)
movq -112(%rbp),%rax
movq -88(%rbp),%r10
imulq %r10,%rax
movq %rax,-120(%rbp)
movq -160(%rbp),%rax
movq %rax,-80(%rbp)
movq -120(%rbp),%rax
movq -80(%rbp),%r10
cltd
idivq %r10
movq %rdx,-128(%rbp)
movq -128(%rbp),%rax
movq -192(%rbp,%rax,8),%r10
movq %r10,-72(%rbp)
movq -72(%rbp),%rax
cmpq $8,%rax
jge .L4
movq -168(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -176(%rbp),%rax
movq $2,%r10
subq %r10,%rax
movq %rax,-40(%rbp)
movq -192(%rbp),%rax
movq %rax,-32(%rbp)
movq -40(%rbp),%rax
movq -32(%rbp),%r10
addq %r10,%rax
movq %rax,-48(%rbp)
movq -184(%rbp),%rax
movq %rax,-24(%rbp)
movq -48(%rbp),%rax
movq -24(%rbp),%r10
imulq %r10,%rax
movq %rax,-56(%rbp)
movq -160(%rbp),%rax
movq %rax,-16(%rbp)
movq -56(%rbp),%rax
movq -16(%rbp),%r10
cltd
idivq %r10
movq %rdx,-64(%rbp)
movq -64(%rbp),%rax
movq -192(%rbp,%rax,8),%r10
movq %r10,-8(%rbp)
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -64(%rbp),%rax
movq -8(%rbp),%r10
movq %r10,-192(%rbp,%rax,8)
jmp .L3
.L4:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

