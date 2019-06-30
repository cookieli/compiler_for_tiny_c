.file "tests/codeGenOfMe/globl.dcf"
.text
.comm	a,8, 8
.comm	b,8, 8
.comm	c,1, 1
.comm	d,1, 1
.comm	e,40,32
.comm	l,10,8
.section .rodata
.LC0:
.string "%d, %d\n"
.LC1:
.string "array e: %d, %d\n"
.LC2:
.string "bool: %d, %d\n"
.LC3:
.string "array l: %d, %d, %d, %d, %d, %d, %d, %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $5,e(%rip)
movq $10,l(%rip)
movq $3,a(%rbp)
movq $5,b(%rbp)
movq b(%rbp),%rax
movq a(%rbp),%r10
addq %r10,%rax
movq %rax,b(%rbp)
movq $4,e+8(%rbp)
movq $5,24+e+8(%rbp)
leaq e+8(%rbp),%rax
movq a(%rbp),%r10
movq b(%rbp),%r11
movq %r11,(%rax,%r10,8)
movb $1,c(%rbp)
movb $0,d(%rbp)
leaq l+8(%rbp),%rax
movq b(%rbp),%r10
movb $1,(%rax,%r10,1)
leaq l+8(%rbp),%rax
movq b(%rbp),%r10
movb (%rax,%r10,1),%r11b
movb %r11b,-27(%rbp)
leaq l+8(%rbp),%rax
movq a(%rbp),%r10
movb -27(%rbp),%r11b
movb %r11b,(%rax,%r10,1)
movq b(%rbp),%rdx
movq a(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq e+8(%rbp),%rax
movq %rax,-26(%rbp)
movq 24+e+8(%rbp),%rax
movq %rax,-18(%rbp)
movq -18(%rbp),%rdx
movq -26(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movsbq d(%rbp),%rdx
movsbq c(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movb l+8(%rbp),%al
movb %al,-10(%rbp)
movb 1+l+8(%rbp),%al
movb %al,-9(%rbp)
movb 2+l+8(%rbp),%al
movb %al,-8(%rbp)
movb 3+l+8(%rbp),%al
movb %al,-7(%rbp)
movb 4+l+8(%rbp),%al
movb %al,-6(%rbp)
movb 5+l+8(%rbp),%al
movb %al,-5(%rbp)
movb 6+l+8(%rbp),%al
movb %al,-4(%rbp)
movb 7+l+8(%rbp),%al
movb %al,-3(%rbp)
movb 8+l+8(%rbp),%al
movb %al,-2(%rbp)
movb 9+l+8(%rbp),%al
movb %al,-1(%rbp)
subq $8,%rsp
movsbq -1(%rbp),%rdi
pushq %rdi
movsbq -2(%rbp),%rdi
pushq %rdi
movsbq -3(%rbp),%rdi
pushq %rdi
movsbq -4(%rbp),%rdi
pushq %rdi
movsbq -5(%rbp),%rdi
pushq %rdi
movsbq -6(%rbp),%r9
movsbq -7(%rbp),%r8
movsbq -8(%rbp),%rcx
movsbq -9(%rbp),%rdx
movsbq -10(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $48,%rsp
leave
ret

