.file "tests/codeGenOfMe/array.dcf"
.text
.section .rodata
.LC0:
.string "%d, %d, %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $64, %rsp
movq $3,-24(%rbp)
movq $4,-16(%rbp)
movq $0,-64(%rbp)
movq $1,-56(%rbp)
movq $3,-48(%rbp)
movq $4,-40(%rbp)
movq $5,-32(%rbp)
movq -16(%rbp),%rax
movq -64(%rbp,%rax,8),%rax
movq %rax,-8(%rbp)
movq -24(%rbp),%rax
movq -8(%rbp),%rax
movq %rax,-64(%rbp,%rax,8)
movq -32(%rbp),%r9
movq -40(%rbp),%r8
movq -48(%rbp),%rcx
movq -56(%rbp),%rdx
movq -64(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

