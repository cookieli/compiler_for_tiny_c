.file "tests/codeGenOfMe/array.dcf"
.text
.section .rodata
.LC0:
.string "%d, %d, %d, %d, %d\n"
.LC1:
.string "the bool array %d, %d, %d, %d, %d, %d, %d"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $80, %rsp
movq $3,-24(%rbp)
movq $4,-16(%rbp)
movq $0,-71(%rbp)
movq $1,-63(%rbp)
movq $3,-55(%rbp)
movq $4,-47(%rbp)
movq $5,-39(%rbp)
movq -16(%rbp),%rax
movq -71(%rbp,%rax,8),%r10
movq %r10,-8(%rbp)
movq -24(%rbp),%rax
movq -8(%rbp),%r10
movq %r10,-71(%rbp,%rax,8)
movb $1,-31(%rbp)
movb $0,-30(%rbp)
movb $0,-29(%rbp)
movb $1,-28(%rbp)
movb $0,-27(%rbp)
movb $1,-26(%rbp)
movb $0,-25(%rbp)
movq -39(%rbp),%r9
movq -47(%rbp),%r8
movq -55(%rbp),%rcx
movq -63(%rbp),%rdx
movq -71(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movsbq -25(%rbp),%rdi
pushq %rdi
movsbq -26(%rbp),%rdi
pushq %rdi
movsbq -27(%rbp),%r9
movsbq -28(%rbp),%r8
movsbq -29(%rbp),%rcx
movsbq -30(%rbp),%rdx
movsbq -31(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
addq $16,%rsp
movq $0,%rax
leave
ret

