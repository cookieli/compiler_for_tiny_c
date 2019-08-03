.file "tests/DataFlow/math_01.dcf"
.text
.section .rodata
.LC0:
.string "10 + 20 is %d (30)\n"
.LC1:
.string "10 - 20 is %d (-10)\n"
.LC2:
.string "10 * 20 is %d (200)\n"
.LC3:
.string "a < b is correct\n"
.LC4:
.string "c < b is incorrect\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $0,%rax
movq %rax,-32(%rbp)
movq %rax,-24(%rbp)
movq %rax,-16(%rbp)
movq %rax,-8(%rbp)
movq $10,%rax
movq $20,%r10
addq %r10,%rax
movq %rax,-24(%rbp)
movq -24(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $10,%rax
movq $20,%r10
subq %r10,%rax
movq %rax,-24(%rbp)
movq -24(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $10,%rax
movq $20,%r10
imulq %r10,%rax
movq %rax,-24(%rbp)
movq -24(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $1,-24(%rbp)
movq $2,-16(%rbp)
movq $2,-8(%rbp)
movq -24(%rbp),%rax
movq -16(%rbp),%r10
cmpq %r10,%rax
jge .L2
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L2:
movq -8(%rbp),%rax
movq -16(%rbp),%r10
cmpq %r10,%rax
jge .L1
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L1
.L1:
leave
ret

