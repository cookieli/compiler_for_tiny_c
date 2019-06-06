.file "tests/codeGenOfMe/array.dcf"
.text
.section .rodata
.LC0:
.string "%d, %d, %d, %d, %d\n"
.LC1:
.string "the bool array %d, %d, %d, %d, %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $96, %rsp
movq $5,-87(%rbp)
movq $7,-39(%rbp)
movq $3,-24(%rbp)
movq $4,-16(%rbp)
movq $0,-79(%rbp)
movq $1,-71(%rbp)
movq $3,-63(%rbp)
movq $4,-55(%rbp)
movq $5,-47(%rbp)
movq -16(%rbp),%rax
movq -79(%rbp,%rax,8),%r10
movq %r10,-8(%rbp)
movq -24(%rbp),%rax
movq -8(%rbp),%r10
movq %r10,-79(%rbp,%rax,8)
movb $1,-31(%rbp)
movb $0,-30(%rbp)
movb $0,-29(%rbp)
movb $1,-28(%rbp)
movb $0,-27(%rbp)
movb $1,-26(%rbp)
movb $0,-25(%rbp)
movq -47(%rbp),%r9
movq -55(%rbp),%r8
movq -63(%rbp),%rcx
movq -71(%rbp),%rdx
movq -79(%rbp),%rsi
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

