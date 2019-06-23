.file "tests/codeGenOfMe/simpleAdd.dcf"
.text
.section .rodata
.LC0:
.string "the sum of %d and %d is %d\n"
.LC1:
.string "the product of %d and %d is %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $96, %rsp
movq $2,-64(%rbp)
movq $2,-40(%rbp)
movq $2,-96(%rbp)
movq $3,-48(%rbp)
movq $4,-24(%rbp)
movq $6,-88(%rbp)
movq -96(%rbp),%rax
movq -88(%rbp),%r10
addq %r10,%rax
movq %rax,-80(%rbp)
movq -48(%rbp),%rax
movq %rax,-16(%rbp)
movq -24(%rbp),%rax
movq %rax,-8(%rbp)
movq -16(%rbp),%rax
movq -8(%rbp),%r10
addq %r10,%rax
movq %rax,-72(%rbp)
movq -96(%rbp),%rax
movq -88(%rbp),%r10
imulq %r10,%rax
movq %rax,-72(%rbp)
movq -80(%rbp),%rcx
movq -88(%rbp),%rdx
movq -96(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -72(%rbp),%rcx
movq -88(%rbp),%rdx
movq -96(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

